package com.example.mohi_pc.myhome;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.net.Uri;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.GreenDao.model.DaoMaster;
import com.GreenDao.model.DaoSession;
import com.GreenDao.model.Room;
import android.widget.Toast;
import com.GreenDao.model.Channel;
import com.GreenDao.model.Memory;

import com.GreenDao.model.WallUnit;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.akexorcist.localizationactivity .LocalizationActivity;

import java.util.HashMap;
import java.util.Map;

public class ChangeNamesActivity extends LocalizationActivity implements RoomFragment.OnFragmentInteractionListener,
        WallUnitFragment.OnFragmentInteractionListener,
        ChannelFragment.OnFragmentInteractionListener,
        MemoryFragment.OnFragmentInteractionListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    Map<Long, String> room_map = new HashMap<Long, String>();
    Map<Long, String> wallUnit_map = new HashMap<Long, String>();
    Map<Long, String> channel_map = new HashMap<Long, String>();
    Map<Long, String> memory_map = new HashMap<Long, String>();

    public void onFragmentInteraction(Uri uri) {
        //you can leave it empty
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_names);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {

        if (!room_map.isEmpty() || !wallUnit_map.isEmpty() || !channel_map.isEmpty() || !memory_map.isEmpty()) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle(R.string.save_dialog_question);
            //alertbox.setMessage("Quit ??? ");

            alertbox.setPositiveButton(R.string.save,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            saveNamesInDB();
                            ChangeNamesActivity.super.onBackPressed();
                        }
                    });

            alertbox.setNeutralButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            ChangeNamesActivity.super.onBackPressed();
                        }
                    });

            alertbox.show();
        }
        else{
            ChangeNamesActivity.super.onBackPressed();
        }

        return;

    }

    @Override
    public void RoomOnDataPass(Long id, String name){
        room_map.put(id,name);
    }
    @Override
    public void WallUnitsOnDataPass(Long id,String name){
        wallUnit_map.put(id,name);
    }
    @Override
    public void ChannelsOnDataPass(Long id, String name){
        channel_map.put(id,name);
    }
    @Override
    public void MemoryOnDataPass(Long id, String name) {  memory_map.put(id,name);}

    public void saveNamesInDB(){

        //begin transaction with DB
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "home_database", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        db.beginTransaction();

        //Room
        for (Map.Entry<Long, String> entry : room_map.entrySet()) {
            Long id = entry.getKey();
            String name = entry.getValue();
            Room room = daoSession.getRoomDao().load(id);
            if(room != null){
                if(room.getName() != name){
                    room.setName(name);
                    daoSession.update(room);
                }
            }
        }

        //WallUnit
        for (Map.Entry<Long, String> entry : wallUnit_map.entrySet()) {
            Long id = entry.getKey();
            String name = entry.getValue();
            WallUnit wu = daoSession.getWallUnitDao().load(id);
            if(wu != null){
                if(wu.getName() != name){
                    wu.setName(name);
                    daoSession.update(wu);
                }
            }
        }

        //Channel
        for (Map.Entry<Long, String> entry : channel_map.entrySet()) {
            Long id = entry.getKey();
            String name = entry.getValue();
            Channel ch = daoSession.getChannelDao().load(id);
            if(ch != null){
                if(ch.getName() != name){
                    ch.setName(name);
                    daoSession.update(ch);
                }
            }
        }

        //Memory
        for (Map.Entry<Long, String> entry : memory_map.entrySet()) {
            Long id = entry.getKey();
            String name = entry.getValue();
            Memory memory = daoSession.getMemoryDao().load(id);
            if(memory != null){
                if(memory.getName() != name){
                    memory.setName(name);
                    daoSession.update(memory);
                }
            }
        }

        //end transaction with DB
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        helper.close();

        //clearing the maps since data is now saved in DB
        room_map.clear();
        wallUnit_map.clear();
        channel_map.clear();
        memory_map.clear();

        Toast.makeText(getApplicationContext(),R.string.changes_saved, Toast.LENGTH_LONG).show();
    }

    public void saveNamesButton(View view)
    {
        saveNamesInDB();
    }

    @Override
    public void onStart() {
        super.onStart();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ChangeNames Page", // TODO: Define a title for the content shown.
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
                "ChangeNames Page", // TODO: Define a title for the content shown.
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

    public void changeLanguage(View v) {

        getSupportFragmentManager().beginTransaction().
                remove(getSupportFragmentManager().findFragmentById(R.id.fragment_wallUnit)).commit();
        getSupportFragmentManager().beginTransaction().
                remove(getSupportFragmentManager().findFragmentById(R.id.fragment_channel)).commit();

        String lang = getLanguage();
        if (lang == "en") {
            setLanguage("ar");
        } else if (lang == "ar") {
            setLanguage("en");
        }
    }
}
