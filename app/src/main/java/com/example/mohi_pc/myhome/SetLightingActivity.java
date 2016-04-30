package com.example.mohi_pc.myhome;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.GreenDao.model.Channel;
import com.GreenDao.model.ChannelDao;
import com.GreenDao.model.DaoMaster;
import com.GreenDao.model.DaoSession;
import com.GreenDao.model.Memory;
import com.GreenDao.model.MemoryDao;
import com.GreenDao.model.MemoryValue;
import com.GreenDao.model.MemoryValueDao;
import com.GreenDao.model.Room;
import com.GreenDao.model.RoomDao;
import com.GreenDao.model.WallUnit;
import com.GreenDao.model.WallUnitDao;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Join;
import de.greenrobot.dao.query.QueryBuilder;

public class SetLightingActivity extends MainActivity {

    ListView roomsList;
    ListView memoriesList;
    DaoSession daoSession;
    private static boolean channelOnTouch = false;
    private static int channelValueOnTouch = 0;
    private static long selectedMemory = -1;
    Map<Long, Integer> channelStateMap = new HashMap<Long, Integer>();
    MemoryValueDao memoryValueDaoRead;
    MemoryValueDao memoryValueDaoWrite;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lighting);

        //initialize DB essentials for Reading from DB
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "home_database", null);
        SQLiteDatabase db = helper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        //load all rooms and memories
        getAllRooms(); //roomsList.setSelection(0);
        getAllMemories();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void getAllMemories() {
        final MemoryDao memoryDao = daoSession.getMemoryDao();

        ArrayList<Memory> listRooms = (ArrayList<Memory>) memoryDao.loadAll();

        if (!memoryDao.loadAll().isEmpty()) {
            ArrayList<Memory> listMemories = (ArrayList<Memory>) memoryDao.loadAll();

            memoriesList = (ListView) findViewById(R.id.listView_memories);
            ArrayAdapter<Memory> adapter = new ArrayAdapter<Memory>(this, android.R.layout.simple_list_item_1, listMemories);
            memoriesList.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), "No memory defined", Toast.LENGTH_SHORT).show();
        }
        memoriesList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int j = 0; j < parent.getChildCount(); j++) {
                            parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                        }
                        view.setBackgroundColor(Color.parseColor("#8ed5f6"));

                        Memory memory = (Memory) memoriesList.getItemAtPosition(position);
                        selectedMemory = memory.getId();
                    }
                });
    }

    public void getAllRooms() {
        RoomDao roomDao = daoSession.getRoomDao();

        if (!roomDao.loadAll().isEmpty()) {
            ArrayList<Room> listRooms = (ArrayList<Room>) roomDao.loadAll();

            roomsList = (ListView) findViewById(R.id.listView_rooms);
            ArrayAdapter<Room> adapter = new ArrayAdapter<Room>(this, android.R.layout.simple_list_item_1, listRooms);
            roomsList.setAdapter(adapter);
            getChannelsByRoomName(listRooms.get(0).getName());

            roomsList.getAdapter().getView(0, roomsList.getChildAt(0), roomsList).setBackgroundColor(Color.BLACK);
            //roomsList.getAdapter().getView(0, null , roomsList).setBackgroundColor(Color.BLACK);

        } else {
            Toast.makeText(getApplicationContext(), "No room defined", Toast.LENGTH_SHORT).show();
        }
        roomsList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int j = 0; j < parent.getChildCount(); j++) {
                            parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                        }
                        view.setBackgroundColor(Color.parseColor("#8ed5f6"));
                        String roomName = parent.getAdapter().getItem(position).toString();
                        getChannelsByRoomName(roomName);
                    }
                });
    }

    public void getChannelsByRoomName(String roomName) {
        ChannelDao channelDao = daoSession.getChannelDao();
        QueryBuilder qb = channelDao.queryBuilder();
        Join wallUnit = qb.join(ChannelDao.Properties.WallUnitId, WallUnit.class);
        Join room = qb.join(wallUnit, WallUnitDao.Properties.RoomId, Room.class, RoomDao.Properties.Id);
        room.where(RoomDao.Properties.Name.eq(roomName));
        List<Channel> roomChannels = qb.list();

        for (int i = 0; i < roomChannels.size(); i++) {
            Long id = roomChannels.get(i).getId();
            Integer state = roomChannels.get(i).getState();
            channelStateMap.put(id, state);
        }

        ListView channelsListView = (ListView) findViewById(R.id.listView_channels);
        ChannelAdapter channelsadapter = new ChannelAdapter(this, R.layout.channel, roomChannels);
        channelsListView.setAdapter(channelsadapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SetLighting Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.mohi_pc.myhome/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SetLighting Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.mohi_pc.myhome/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class ChannelAdapter extends ArrayAdapter<Channel> {

        public ChannelAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ChannelAdapter(Context context, int resource, List<Channel> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.channel, null);
            }

            final Channel channel = getItem(position);

            if (channel != null) {
                SeekBar sk = (SeekBar) v.findViewById(R.id.channelSeekBar);
                TextView tk = (TextView) v.findViewById(R.id.textViewSeekBar);
                String channelInfo = channel.getWallUnit().getName().concat("- ").concat(channel.getName());
                tk.setText(channelInfo);

                if (sk != null) {
                    sk.setMax(100);
                    sk.setProgress(channel.getState());
                    sk.setEnabled(true);
                    sk.setContentDescription(channel.toString());
                    sk.setOnSeekBarChangeListener(
                        new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                channelStateMap.put(channel.getId(), seekBar.getProgress());
                                sendPacket();
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                channelOnTouch = true;
                                channelValueOnTouch = seekBar.getProgress();
                            }

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int arg1, boolean fromUser) {
                                //packet request must be sent while arg1 is even
                                if (channelOnTouch == true) {
                                    if (Math.abs(channelValueOnTouch - arg1) == 5) {
                                        sendPacket();
                                        channelValueOnTouch = arg1;
                                    }
                                }
                            }
                        }
                    );
                }
            }
            return v;
        }
    }

    public void sendPacket() {
        Intent intent = new Intent(this, UsbCommunicationService.class);
        Bundle bundle = new Bundle();
        bundle.putInt("MESSAGE_TYPE", 1);
        bundle.putString("COM_PORT", "W");
        intent.putExtras(bundle);
        startService(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        channelOnTouch = false;
    }

    //save memory-settings to DB
    public void saveLightSetting(View v) {

        if (selectedMemory != -1) {

            memoryValueDaoRead = daoSession.getMemoryValueDao();
            DaoMaster.DevOpenHelper helperWrite = new DaoMaster.DevOpenHelper(this, "home_database", null);
            SQLiteDatabase dbWrite = helperWrite.getReadableDatabase();
            DaoMaster daoMasterWrite = new DaoMaster(dbWrite);
            DaoSession daoSessionWrite = daoMasterWrite.newSession();
            memoryValueDaoWrite = daoSessionWrite.getMemoryValueDao();

            QueryBuilder qb;
            qb = memoryValueDaoRead.queryBuilder();
            qb.where(MemoryValueDao.Properties.MemoryId.eq(selectedMemory));
            List<MemoryValue> memoryValueList = qb.list();

            if (!channelStateMap.isEmpty()) {
                for (Map.Entry<Long, Integer> entry : channelStateMap.entrySet()) {

                    Long channelId = entry.getKey();
                    Integer channelState = entry.getValue();

                    boolean flag = false;
                    //do this search more efficient
                    for (int i = 0; i < memoryValueList.size(); i++) {
                        MemoryValue mv = memoryValueList.get(i);
                        if (mv.getChannelId() == channelId) {
                            //update
                            if (mv.getValue() != channelState) {
                                mv.setValue(channelState);
                                daoSessionWrite.update(mv);
                            }
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        //insert
                        MemoryValue newMV = new MemoryValue();
                        newMV.setChannelId(channelId);
                        newMV.setMemoryId(selectedMemory);
                        newMV.setValue(channelState);
                        daoSessionWrite.insert(newMV);
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "select a Room", Toast.LENGTH_SHORT).show();
            }

            daoSessionWrite.clear();
            dbWrite.close();
            helperWrite.close();
        } else {
            Toast.makeText(getApplicationContext(), "select a memory", Toast.LENGTH_SHORT).show();
        }
    }
}
