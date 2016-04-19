package com.example.mohi_pc.myhome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

public class SetPasswordActivity extends MainActivity {
    Button b;
    EditText ed;
    //public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String password = "";

    SharedPreferences sharedpreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        /*ed=(EditText)findViewById(R.id.editText_setPassword);
        b=(Button)findViewById(R.id.button_save_password);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p  = ed.getText().toString();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(password, p);
                editor.commit();
            }
        });*/
    }
}
