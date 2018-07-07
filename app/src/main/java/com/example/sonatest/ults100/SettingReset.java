package com.example.sonatest.ults100;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingReset extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "sametsMessage";

    private Button bResetSettings;
    private Button bResetDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_reset);

        bResetSettings = (Button)findViewById(R.id.bResetSettings);
        bResetDatabase = (Button)findViewById(R.id.bResetDatabase);
        bResetSettings.setOnClickListener(this);
        bResetDatabase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bResetSettings:
                SharedPreferences sharedPref = getSharedPreferences("SettingInfo", Context.MODE_PRIVATE);
                sharedPref.edit().clear().commit();
                Toast.makeText(getApplicationContext(),"Ayarlar Sıfırlandı",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SettingReset: bResetSettings: SharedPreferences: All SettingInfo data has been removed");
                break;
            case R.id.bResetDatabase:
                this.deleteDatabase("signals.db");
                Toast.makeText(getApplicationContext(),"Veritabanı Sıfırlandı",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SettingReset: bResetDatabase: Database has been removed");
                break;
        }
    }
}
