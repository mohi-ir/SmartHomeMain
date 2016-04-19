package com.example.mohi_pc.myhome;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.GreenDao.model.DaoMaster;
import com.GreenDao.model.DaoSession;
import com.GreenDao.model.Function;
import com.GreenDao.model.FunctionDao;


import android.view.View;


public class CentralSettingActivity extends MainActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_setting);


        /*DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "home_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        FunctionDao functionDao = daoSession.getFunctionDao();

        Function function = new Function();
        function.setName("light");
        function.setDescriptor("light");
        functionDao.insert(function);

        daoSession.clear();
        db.close();
        helper.close();*/

        //--- Setting password for authenticated actions


}
    public void setPassword(View v) {
        // Create an Intent to start the second activity: go to lighting setting mode
        Intent intent = new Intent(this, SetPasswordActivity.class);
        startActivity(intent);
    }

}
