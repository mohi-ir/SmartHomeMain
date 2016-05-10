package com.example.mohi_pc.myhome;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;
import android.widget.Toast;

import com.GreenDao.model.Channel;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.List;

import java.util.ArrayList;
import android.os.Binder;

public class UsbToSerialComService extends Service {

    private UsbDevice device;
    private UsbManager usbManager;
    private UsbDeviceConnection connection;
    private Context mContext;
    private static final int deviceVendorId =1659;

    private UsbSerialInterface serialPort;
    public static boolean serialPortConnected = false;
    private static final int BAUD_RATE = 9600;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    final static int REPEAT_WRITE =5;
    List<String> readBuffer = new ArrayList<String>();
    private static StringBuffer readLine= new StringBuffer();
    final static Long ackTimeout = 100L;

    SharedPrefAdapter spObject;
    PacketCreation packetCreation;
    private static String netAddress;

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            serialPort.read(mCallback);
            Toast.makeText(getApplicationContext(), "USB-to-Serial established", Toast.LENGTH_SHORT).show();
          }
    }

    public UsbToSerialComService() {
    }

    @Override
    public void onCreate() {

        HandlerThread thread = new HandlerThread("StartedService",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        mContext=getApplicationContext();
        spObject =  new SharedPrefAdapter(mContext);
        netAddress = spObject.prefGet(spObject.COMMUNICATION_PREF,spObject.NET_ADDRESS_ROW);
        packetCreation = new PacketCreation(netAddress);

        connectToUsbDevice();
        if(device != null && connection!=null){
            establishSerialConnection();
        }
        else{
            Toast.makeText(getApplicationContext(), "usb-to-serial connection problem", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(serialPortConnected){
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            mServiceHandler.sendMessage(msg);
        }
        else{
            Toast.makeText(getApplicationContext(), "usb-to-serial connection problem", Toast.LENGTH_LONG).show();
        }
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        Log.d("SERVICE","destroy");
        stopSelf();
        super.onDestroy();
    }

    //connect to the usb-to-serial connector
    private void connectToUsbDevice()
    {
        usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                if (device.getVendorId() == deviceVendorId) {
                    connection = usbManager.openDevice(device);
                    keep = false;
                }
                if (!keep) break;
            }
        }
    }

    //Establish a serial port within usb-to-serial convertor
    private void establishSerialConnection()
    {
        serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
        if (serialPort != null)
        {
            if (serialPort.open())
            {
                serialPortConnected =true;
                serialPort.setBaudRate(BAUD_RATE);
                serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
            }
            else
            {
                Log.d("SERIAL", "serial port could not be opened");
            }
        }

    }

    // A callback for received data
    private UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] arg0) {

        String dataByte = new String(arg0);

        if (!dataByte.equals(packetCreation.DELIMITER_END_OF_PACKET))
        {
            readLine.append(dataByte);
        }
        else
        {
            readLine.append(packetCreation.DELIMITER_END_OF_PACKET);
            Log.d("READ",readLine.toString());
            runParser();
        }
        }
    };

    //write data to serialPort
    public void write(String data){

        if (serialPortConnected) {
            serialPort.write(data.getBytes());
        }
        }

    /**
     * Creating a light-control-dim packet for channel and send it via AckProtocole
     * @param ch
     * @param value
     */
    public void sendLightControlPacketDim(Channel ch, String value) {

        StringBuffer result = packetCreation.CP_light_control_dim(ch, value);
        write(result.toString());
    }

    public void sendLightControlPacketMemorySet(Channel ch, String value, String memNumber, String type){

        StringBuffer [] result = packetCreation.CP_light_control_memory(ch, value ,memNumber , type);
        writeWithAckNewThread(result[0].toString(),result[1].toString());
    }

    /**
     * Sending a packet on a new thread and waiting for its ack to come
     * @param packet
     */
    private void writeWithAckNewThread(final String packet,final String ack){

        final Thread thread = new Thread(){
            public void run(){

                for(int i=0;i<REPEAT_WRITE;i++) {
                    try{
                        Log.d("SEND","run".concat(String.valueOf(i)).concat("  ").concat(packet));
                        serialPort.write(packet.getBytes());

                        //wait for awhile
                        TimeUnit.MILLISECONDS.sleep(ackTimeout);

                        //if you recieve ack, do not continue sending
                        if(readBuffer.contains(ack.trim())){
                            readBuffer.remove(ack);
                            Log.d("ACK",ack);
                            break;
                        }

                    }catch (InterruptedException e){
                        Log.d("SEND","exception");
                    }
                }
            }
        };
        thread.start();
        //thread.interrupt();
    }


    public class LocalBinder extends Binder {
        UsbToSerialComService getService() {
            // Return this instance of LocalService so clients can call public methods
            return UsbToSerialComService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void runParser(){

        String tokenize = readLine.toString();
        String [] splitMessageId = tokenize.split(packetCreation.DELIMITER_END_OF_MESSAGEID);
        String messageId = splitMessageId[0];
        String [] splitWords = splitMessageId[1].split(packetCreation.DELIMITER_END_OF_WORD);
        String nodeAdress = splitWords[1];

        //if this packet belongs to this network
        if(splitWords[0].equals(netAddress)){

            //check if messageId is a valid messageId
            if(packetCreation.Messages.containsValue(messageId)) {

                //put packet in buffer
                readBuffer.add((messageId + packetCreation.DELIMITER_END_OF_MESSAGEID + nodeAdress).trim());
            }
        }

        readLine= new StringBuffer();
    }

}
