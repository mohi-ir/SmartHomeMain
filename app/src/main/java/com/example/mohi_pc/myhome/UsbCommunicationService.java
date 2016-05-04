package com.example.mohi_pc.myhome;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.os.HandlerThread;
import android.os.Process;


import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UsbCommunicationService extends Service {

    private UsbDevice device;
    private UsbManager usbManager;
    private UsbDeviceConnection connection;
    private Context mContext;
    private UsbSerialInterface serialPortRead;
    private UsbSerialInterface serialPortWrite;
    private static boolean WritePortConnected = false;
    private static boolean ReadPortConnected = false;

    static final int MSG_READ = 0;
    static final int MSG_SEND_PACKET = 1;
    static final String COMMUNICATION_PORT_READ = "R";
    static final String COMMUNICATION_PORT_WRITE = "W";

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private static final int BAUD_RATE = 9600;
    private static final int deviceVendorId =1659;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_READ:
                    if(ReadPortConnected) {
                        //Toast.makeText(getApplicationContext(), "Service is listening...", Toast.LENGTH_SHORT).show();
                        serialPortRead.read(mCallback);
                    }
                    break;
                case MSG_SEND_PACKET:
                    if(WritePortConnected) {
                        serialPortWrite.write("packet - ".getBytes());
                        Toast t = new Toast(getApplicationContext());
                        t.cancel();
                        //Toast.makeText(getApplicationContext(), "packet sent", Toast.LENGTH_SHORT).show();
                    }
                    WritePortConnected =false;
                    serialPortWrite.close();
                    stopSelf(msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }

    @Override
    public void onCreate() {

        HandlerThread thread = new HandlerThread("StartedService",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        //stablish usb-to-serial communication
        mContext=getApplicationContext();
        connectToUsbDevice();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.what = intent.getExtras().getInt("MESSAGE_TYPE");

        String CommunicationPort = intent.getExtras().getString("COM_PORT");

        if(CommunicationPort.equals(COMMUNICATION_PORT_READ)) {
            Log.d("SERIAL","if 1");
            establishSerialToUsbCommunicationRead();
        }
        else if(CommunicationPort.equals(COMMUNICATION_PORT_WRITE)){
            Log.d("SERIAL","if 1");
            establishSerialToUsbCommunicationWrite();
        }

        mServiceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    //--- start code for serial communication
    public void connectToUsbDevice()
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

    // A callback for received data must be defined
    private UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;

            data = new String(arg0);
            Log.d("SERIAL_DATA_COMMING",data);
        }
    };

    public void establishSerialToUsbCommunicationWrite()
    {
        if(device!=null && connection!=null)
        {
            serialPortWrite = UsbSerialDevice.createUsbSerialDevice(device, connection);
            if (serialPortWrite != null)
            {
                Log.d("SERIAL","serialPort not null");

                if (serialPortWrite.open())
                {
                    WritePortConnected = true;
                    serialPortWrite.setBaudRate(BAUD_RATE);
                    serialPortWrite.setDataBits(UsbSerialInterface.DATA_BITS_8);
                    serialPortWrite.setStopBits(UsbSerialInterface.STOP_BITS_1);
                    serialPortWrite.setParity(UsbSerialInterface.PARITY_NONE);
                    serialPortWrite.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                }
                else
                {
                    Log.d("SERIAL", "serial port could not be opened");
                }
            }

        }
    }
    public void establishSerialToUsbCommunicationRead()
    {
        if(device!=null && connection!=null)
        {
            serialPortRead = UsbSerialDevice.createUsbSerialDevice(device, connection);
            if (serialPortRead != null)
            {
                Log.d("SERIAL","serialPort not null");

                if ( serialPortRead.open())
                {
                    ReadPortConnected = true;
                    serialPortRead.setBaudRate(BAUD_RATE);
                    serialPortRead.setDataBits(UsbSerialInterface.DATA_BITS_8);
                    serialPortRead.setStopBits(UsbSerialInterface.STOP_BITS_1);
                    serialPortRead.setParity(UsbSerialInterface.PARITY_NONE);
                    serialPortRead.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                }
                else
                {
                    Log.d("SERIAL", "serial port could not be opened");
                }
            }

        }
    }

}
