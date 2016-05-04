package com.example.mohi_pc.myhome;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import com.GreenDao.model.Channel;
import com.GreenDao.model.DaoMaster;

import com.GreenDao.model.DaoSession;
import com.GreenDao.model.Function;
import com.GreenDao.model.Memory;
import com.GreenDao.model.MemoryType;
import com.GreenDao.model.Room;
import com.GreenDao.model.WallUnit;
import com.GreenDao.model.WuType;
import com.akexorcist.localizationactivity.LocalizationActivity;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends LocalizationActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static final String MyPREFERENCES = "MyPrefs" ;

    boolean isStartUsbCommunication =false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // network adress:
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("networkAddress", "belabelabela");
        editor.commit();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "home_database", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        //DaoMaster.dropAllTables(db,true);
        DaoMaster.createAllTables(db, true);
        helper.close();
        //loadFixtures();

        //listening to usb-serial port for incomming data
       /* Intent intent = new Intent(this, UsbCommunicationService.class);
        Bundle bundle = new Bundle();
        bundle.putInt("MESSAGE_TYPE", 0);
        bundle.putString("COM_PORT", "R");
        intent.putExtras(bundle);
        startService(intent); */
    }

    public void loadFixtures()
    {

        //---- data fixtures -----
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "home_database", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        db.beginTransaction();

        //function
        daoSession.getFunctionDao();
        Function lighting  = new Function();
        lighting.setName("light");
        lighting.setDescriptor("light");
        daoSession.insert(lighting);
        Function sound  = new Function();
        sound.setName("sound");
        sound.setDescriptor("sound");
        daoSession.insert(sound);

        //Room
        daoSession.getRoomDao();
        Room bedroom = new Room();
        bedroom.setName("bedroom");
        bedroom.setDescriptor("bedroom");
        daoSession.insert(bedroom);
        Room kitchen = new Room();
        kitchen.setName("kitchen");
        kitchen.setDescriptor("kitchen");
        daoSession.insert(kitchen);
        Room livingRoom=new Room();
        livingRoom.setName("living room");
        livingRoom.setDescriptor("livingRoom");
        daoSession.insert(livingRoom);
        Room balcony = new Room();
        balcony.setName("balcony");
        balcony.setDescriptor("balcony");
        daoSession.insert(balcony);

        //memoryType
        daoSession.getMemoryTypeDao();
        MemoryType mt1=new MemoryType();
        mt1.setName("central hub");
        mt1.setDescriptor("CH");
        daoSession.insert(mt1);
        MemoryType mt2=new MemoryType();
        mt2.setName("wall unit");
        mt2.setDescriptor("WU");
        daoSession.insert(mt2);

        //WuType
        daoSession.getWuTypeDao();
        WuType wu1 = new WuType();
        wu1.setName("RF");
        wu1.setDescriptor("RF");
        daoSession.insert(wu1);
        WuType wu2 = new WuType();
        wu2.setName("RS");
        wu2.setDescriptor("RS");
        daoSession.insert(wu2);

        //wallunits
        //bedroom
        WallUnit bedroomWU1 = new WallUnit();
        bedroomWU1.setAddress("986734");
        bedroomWU1.setWuType(wu1);
        bedroomWU1.setRoom(bedroom);
        bedroomWU1.setName("bedroom_wallUnit_1");
        daoSession.insert(bedroomWU1);
        WallUnit bedroomWU2 = new WallUnit();
        bedroomWU2.setAddress("239872");
        bedroomWU2.setWuType(wu1);
        bedroomWU2.setRoom(bedroom);
        bedroomWU2.setName("bedroom_wallUnit_2");
        daoSession.insert(bedroomWU2);
        //kitchen
        WallUnit kitchenWU1=new WallUnit();
        kitchenWU1.setRoom(kitchen);
        kitchenWU1.setAddress("34765");
        kitchenWU1.setWuType(wu1);
        kitchenWU1.setName("kitchen_wallUnit_1");
        daoSession.insert(kitchenWU1);
        //living room
        WallUnit livingRoomWU1 = new WallUnit();
        livingRoomWU1.setAddress("876543");
        livingRoomWU1.setRoom(livingRoom);
        livingRoomWU1.setWuType(wu1);
        livingRoomWU1.setName("living_wallUnit_1");
        daoSession.insert(livingRoomWU1);
        WallUnit livingRoomWU2 = new WallUnit();
        livingRoomWU2.setWuType(wu2);
        livingRoomWU2.setParentId(wu2.getId());
        livingRoomWU2.setAddress("98765");
        livingRoomWU2.setRoom(livingRoom);
        livingRoomWU2.setName("living_wallUnit_2");
        daoSession.insert(livingRoomWU2);
        WallUnit livingRoomWU3 = new WallUnit();
        livingRoomWU3.setRoom(livingRoom);
        livingRoomWU3.setAddress("87654");
        livingRoomWU3.setWuType(wu1);
        livingRoomWU3.setName("living_wallUnit_3");
        daoSession.insert(livingRoomWU3);
        //balcony
        WallUnit balconyWU1 = new WallUnit();
        balconyWU1.setAddress("876654");
        balconyWU1.setRoom(balcony);
        balconyWU1.setWuType(wu1);
        balconyWU1.setName("balcony_wallUnit_1");
        daoSession.insert(balconyWU1);

        //channels
        //bedroomWU1
        daoSession.getChannelDao();
        Channel bedroomWU1Ch1 = new Channel();
        bedroomWU1Ch1.setAddress("45764");
        bedroomWU1Ch1.setFunction(lighting);
        bedroomWU1Ch1.setState(5);
        bedroomWU1Ch1.setWallUnit(bedroomWU1);
        bedroomWU1Ch1.setName("bed_wu1_Ch_1");
        daoSession.insert(bedroomWU1Ch1);
        Channel bedroomWU1Ch2 = new Channel();
        bedroomWU1Ch2.setAddress("3764");
        bedroomWU1Ch2.setFunction(lighting);
        bedroomWU1Ch2.setState(0);
        bedroomWU1Ch2.setWallUnit(bedroomWU1);
        bedroomWU1Ch2.setName("bed_wu1_Ch_2");
        daoSession.insert(bedroomWU1Ch2);
        Channel bedroomWU1Ch3 = new Channel();
        bedroomWU1Ch3.setAddress("45761");
        bedroomWU1Ch3.setFunction(lighting);
        bedroomWU1Ch3.setState(67);
        bedroomWU1Ch3.setWallUnit(bedroomWU1);
        bedroomWU1Ch3.setName("bed_wu1_Ch_3");
        daoSession.insert(bedroomWU1Ch3);
        //bedroomWU2
        Channel bedroomWU2Ch1 = new Channel();
        bedroomWU2Ch1.setAddress("45764");
        bedroomWU2Ch1.setFunction(lighting);
        bedroomWU2Ch1.setState(20);
        bedroomWU2Ch1.setWallUnit(bedroomWU2);
        bedroomWU2Ch1.setName("bed_wu2_Ch_1");
        daoSession.insert(bedroomWU2Ch1);
        Channel bedroomWU2Ch2 = new Channel();
        bedroomWU2Ch2.setAddress("45764");
        bedroomWU2Ch2.setFunction(sound);
        bedroomWU2Ch2.setState(22);
        bedroomWU2Ch2.setWallUnit(bedroomWU2);
        bedroomWU2Ch2.setName("bed_wu2_Ch_2");
        daoSession.insert(bedroomWU2Ch2);
        //kitchenWU1
        Channel kitchenWU1Ch1 = new Channel();
        kitchenWU1Ch1.setAddress("45764");
        kitchenWU1Ch1.setFunction(lighting);
        kitchenWU1Ch1.setState(100);
        kitchenWU1Ch1.setWallUnit(kitchenWU1);
        kitchenWU1Ch1.setName("kit_wu1_Ch_1");
        daoSession.insert(kitchenWU1Ch1);
        Channel kitchenWU1Ch2 = new Channel();
        kitchenWU1Ch2.setAddress("45764");
        kitchenWU1Ch2.setFunction(lighting);
        kitchenWU1Ch2.setState(40);
        kitchenWU1Ch2.setWallUnit(kitchenWU1);
        kitchenWU1Ch2.setName("kit_wu1_Ch_2");
        daoSession.insert(kitchenWU1Ch2);
        Channel kitchenWU1Ch3 = new Channel();
        kitchenWU1Ch3.setAddress("45764");
        kitchenWU1Ch3.setFunction(lighting);
        kitchenWU1Ch3.setState(55);
        kitchenWU1Ch3.setWallUnit(kitchenWU1);
        kitchenWU1Ch3.setName("kit_wu1_Ch_3");
        daoSession.insert(kitchenWU1Ch3);
        //livingRoomWU1
        Channel LivingRoomWU1Ch1 = new Channel();
        LivingRoomWU1Ch1.setAddress("45764");
        LivingRoomWU1Ch1.setFunction(lighting);
        LivingRoomWU1Ch1.setState(12);
        LivingRoomWU1Ch1.setWallUnit(livingRoomWU1);
        LivingRoomWU1Ch1.setName("liv_wu1_Ch_1");
        daoSession.insert(LivingRoomWU1Ch1);
        Channel LivingRoomWU1Ch2 = new Channel();
        LivingRoomWU1Ch2.setAddress("45764");
        LivingRoomWU1Ch2.setFunction(lighting);
        LivingRoomWU1Ch2.setState(79);
        LivingRoomWU1Ch2.setWallUnit(livingRoomWU1);
        LivingRoomWU1Ch2.setName("liv_wu1_Ch_2");
        daoSession.insert(LivingRoomWU1Ch2);
        //livingRoomWU2
        Channel LivingRoomWU2Ch1 = new Channel();
        LivingRoomWU2Ch1.setAddress("45764");
        LivingRoomWU2Ch1.setFunction(lighting);
        LivingRoomWU2Ch1.setState(34);
        LivingRoomWU2Ch1.setWallUnit(livingRoomWU2);
        LivingRoomWU2Ch1.setName("liv_wu2_Ch_1");
        daoSession.insert(LivingRoomWU2Ch1);
        Channel LivingRoomWU2Ch2 = new Channel();
        LivingRoomWU2Ch2.setAddress("45");
        LivingRoomWU2Ch2.setFunction(lighting);
        LivingRoomWU2Ch2.setState(50);
        LivingRoomWU2Ch2.setWallUnit(livingRoomWU2);
        LivingRoomWU2Ch2.setName("liv_wu2_Ch_2");
        daoSession.insert(LivingRoomWU2Ch2);
        Channel LivingRoomWU2Ch3 = new Channel();
        LivingRoomWU2Ch3.setAddress("464");
        LivingRoomWU2Ch3.setFunction(lighting);
        LivingRoomWU2Ch3.setState(77);
        LivingRoomWU2Ch3.setWallUnit(livingRoomWU2);
        LivingRoomWU2Ch3.setName("liv_wu2_Ch_3");
        daoSession.insert(LivingRoomWU2Ch3);
        //livingRoomWU3
        Channel LivingRoomWU3Ch1 = new Channel();
        LivingRoomWU3Ch1.setAddress("45764");
        LivingRoomWU3Ch1.setFunction(lighting);
        LivingRoomWU3Ch1.setState(67);
        LivingRoomWU3Ch1.setWallUnit(livingRoomWU3);
        LivingRoomWU3Ch1.setName("liv_wu3_Ch_1");
        daoSession.insert(LivingRoomWU3Ch1);
        Channel LivingRoomWU3Ch2 = new Channel();
        LivingRoomWU3Ch2.setAddress("45764");
        LivingRoomWU3Ch2.setFunction(lighting);
        LivingRoomWU3Ch2.setState(88);
        LivingRoomWU3Ch2.setWallUnit(livingRoomWU3);
        LivingRoomWU3Ch2.setName("liv_wu3_Ch_2");
        daoSession.insert(LivingRoomWU3Ch2);
        //balconyWU1
        Channel balconyWU1Ch1 = new Channel();
        balconyWU1Ch1.setAddress("45764");
        balconyWU1Ch1.setFunction(lighting);
        balconyWU1Ch1.setState(57);
        balconyWU1Ch1.setWallUnit(balconyWU1);
        balconyWU1Ch1.setName("bal_wu1_Ch_1");
        daoSession.insert(balconyWU1Ch1);
        Channel balconyWU1Ch2 = new Channel();
        balconyWU1Ch2.setAddress("45764");
        balconyWU1Ch2.setFunction(lighting);
        balconyWU1Ch2.setState(14);
        balconyWU1Ch2.setWallUnit(balconyWU1);
        balconyWU1Ch2.setName("bal_wu1_Ch_2");
        daoSession.insert(balconyWU1Ch2);

        Memory Enter = new Memory();
        Enter.setName("Enter");
        Enter.setMemoryType(mt1);
        daoSession.insert(Enter);
        Memory Exit = new Memory();
        Exit.setName("Exit");
        Exit.setMemoryType(mt1);
        daoSession.insert(Exit);
        Memory night = new Memory();
        night.setName("night");
        night.setMemoryType(mt1);
        daoSession.insert(night);
        Memory U1 = new Memory();
        U1.setName("U1");
        U1.setMemoryType(mt1);
        daoSession.insert(U1);
        Memory U2 = new Memory();
        U2.setName("U2");
        U2.setMemoryType(mt1);
        daoSession.insert(U2);
        Memory U3 = new Memory();
        U3.setName("U3");
        U3.setMemoryType(mt1);
        daoSession.insert(U3);
        Memory U4 = new Memory();
        U4.setName("U4");
        U4.setMemoryType(mt1);
        daoSession.insert(U4);
        Memory U5 = new Memory();
        U5.setName("U5");
        U5.setMemoryType(mt1);
        daoSession.insert(U5);
        Memory U6 = new Memory();
        U6.setName("U6");
        U6.setMemoryType(mt1);
        daoSession.insert(U6);
        Memory U7 = new Memory();
        U7.setName("U7");
        U7.setMemoryType(mt1);
        daoSession.insert(U7);
        Memory U8 = new Memory();
        U8.setName("U8");
        U8.setMemoryType(mt1);
        daoSession.insert(U8);
        Memory U9 = new Memory();
        U9.setName("U9");
        U9.setMemoryType(mt1);
        daoSession.insert(U9);
        Memory U10 = new Memory();
        U10.setName("U11");
        U10.setMemoryType(mt1);
        daoSession.insert(U10);
        Memory U11 = new Memory();
        U11.setName("U10");
        U11.setMemoryType(mt1);
        daoSession.insert(U11);
        Memory U12 = new Memory();
        U12.setName("U12");
        U12.setMemoryType(mt1);
        daoSession.insert(U12);
        Memory U13 = new Memory();
        U13.setName("U13");
        U13.setMemoryType(mt1);
        daoSession.insert(U13);
        Memory U14 = new Memory();
        U14.setName("U14");
        U14.setMemoryType(mt1);
        daoSession.insert(U14);
        Memory U15 = new Memory();
        U15.setName("U15");
        U15.setMemoryType(mt1);
        daoSession.insert(U15);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        helper.close();
    }

    public void sendPacket(View v)
    {
        Intent intent = new Intent(this, SetLightingActivity.class);
        startActivity(intent);
    }

    public void changeLanguage(View v) {
        String lang = getLanguage();
        if (lang == "en") {
            setLanguage("ar");
        } else if (lang == "ar") {
            setLanguage("en");
        }
    }
    public void changeNames(View view){
        Intent intent = new Intent(this, ChangeNamesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
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
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.mohi_pc.myhome/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
