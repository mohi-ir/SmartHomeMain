package com.example.mohi_pc.myhome;

import com.GreenDao.model.Channel;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by Xarrin on 5/7/2016.
 */
public class PacketCreation {

    public static Map<String , String> Messages = new HashMap<String, String >() {{
        put("MESSAGE_ID_LIGHT_DIMMER_RF","+LIGDIM");
        put("MESSAGE_ID_MEMORY_SET_RF ", "+LIGMEM");
        put("MESSAGE_ID_LIGHT_DIMMER_RS485" , "+LIG485");
        put("MESSAGE_ID_MEMORY_SET_RS485" , "+LIGMEM485");
    }};

    final static String RS_DEVICE = "RS";
    private static String netAddress;

    public final String DELIMITER_END_OF_PACKET = "\r";
    public final String DELIMITER_END_OF_WORD = ",";
    public final String DELIMITER_END_OF_MESSAGEID = ":";


    public PacketCreation(String netAddress){
        this.netAddress = netAddress;
    }

    /**
     * creating packet for light-control-dim
     * @param ch
     * @param value
     * @return
     */
    public StringBuffer CP_light_control_dim(Channel ch, String value){

        StringBuffer res = new StringBuffer();

        boolean RS485 = false;
        if(ch.getWallUnit().getWuType().getName().equals(RS_DEVICE)) RS485 = true;

        res.append ((RS485 ? Messages.get("MESSAGE_ID_LIGHT_DIMMER_RS485") : Messages.get("MESSAGE_ID_LIGHT_DIMMER_RF")));
        res .append(DELIMITER_END_OF_MESSAGEID);
        res .append(netAddress);
        res .append( DELIMITER_END_OF_WORD);
        res.append( ch.getWallUnit().getAddress());
        res .append( DELIMITER_END_OF_WORD);
        res .append( (RS485 ? ch.getWallUnit().getWallUnit().getAddress() : ""));
        res .append( DELIMITER_END_OF_WORD);
        res .append( ch.getAddress());
        res .append( DELIMITER_END_OF_WORD);
        res .append( value);
        res .append( DELIMITER_END_OF_PACKET );

        return res;
    }

    /**
     * creating packet for light-control-memory-set
     * @param ch
     * @param value
     * @param memNumber
     * @param type
     * @return
     */
    public StringBuffer [] CP_light_control_memory(Channel ch, String value,String memNumber, String type) {
        StringBuffer res = new StringBuffer();
        StringBuffer ack = new StringBuffer();
        StringBuffer [] resArray = new StringBuffer[2];
        boolean RS485 = false;
        if(ch.getWallUnit().getWuType().getName().equals(RS_DEVICE)) RS485 = true;

        res .append((RS485 ? Messages.get("MESSAGE_ID_MEMORY_SET_RS485") : Messages.get("MESSAGE_ID_MEMORY_SET_RF")));
        res.append( DELIMITER_END_OF_MESSAGEID);
        res .append( netAddress);
        res .append( DELIMITER_END_OF_WORD);
        res .append( ch.getWallUnit().getAddress());
        res .append( DELIMITER_END_OF_WORD);
        res .append( (RS485 ? ch.getWallUnit().getWallUnit().getAddress() : ""));
        res .append( DELIMITER_END_OF_WORD);
        res .append( ch.getAddress());
        res .append( DELIMITER_END_OF_WORD);
        res .append( type);
        res .append( DELIMITER_END_OF_WORD);
        res .append(memNumber);
        res .append(DELIMITER_END_OF_WORD);
        res .append( value);
        res .append( DELIMITER_END_OF_PACKET);

        ack.append((RS485 ? Messages.get("MESSAGE_ID_MEMORY_SET_RS485") : Messages.get("MESSAGE_ID_MEMORY_SET_RF")));
        ack.append(DELIMITER_END_OF_MESSAGEID);
        ack.append(ch.getWallUnit().getAddress());

        resArray [0] = res;
        resArray [1] = ack;

        return resArray;
    }

}
