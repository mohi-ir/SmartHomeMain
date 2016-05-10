package com.example.mohi_pc.myhome;

import android.content.SharedPreferences;
import android.app.Activity;
import android.util.Log;
import android.content.Context;

/**
 * Created by Xarrin on 5/7/2016.
 */
public class SharedPrefAdapter extends Activity {

    public final String COMMUNICATION_PREF = "communication_pref";

    public final String NET_ADDRESS_ROW = "netAddress";

    private final Context context;

    // Default value if row does not exist in preference
    public static final String rowExistence = "Row did not exist";

    public SharedPrefAdapter(Context context){
        super();
        this.context = context;
    }

    public String prefGet(String preferenceName, String rowId) {
        SharedPreferences preferenceObject = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE);
        String value = preferenceObject.getString(rowId, rowExistence);
        return value;
    }

    public void prefSet(String preferenceName, String rowId, String value){

        SharedPreferences preferenceObject = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferenceObject.edit();
        editor.putString(rowId, value);
        editor.commit();
    }
}