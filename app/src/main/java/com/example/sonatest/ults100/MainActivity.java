package com.example.sonatest.ults100;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "sametsMessage";

    public static ArrayList<Byte> sendPac;
    public static byte[] readBuf;
    public static boolean recOrCur;     //kayitli olan mı o anki data mi

    private int adetMA = 0;

    public static final int led_islemi = 1;
    public static final int adc_data_islemi = 4;
    public static final int sinyal_yakalama_ayarlari = 5;
    public static final int adc_ornek_gonder = 8;
    public static final int frekans_sec = 9;
    public static final int adc_gain_set = 11;
    public static final int char_sender = 16;
    public static final int batarya_durum = 17;

    private Button bConnect;
    private Button bConfig;
    private Button bDrawGraphics;
    private Button bSettingReset;
    private CheckBox cbLed1;
    private CheckBox cbLed2;
    private TextView receivedTV;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Bluetooth.MESSAGE_READ) {
                try {
                    readBuf = (byte[]) msg.obj;

                    switch (readBuf[3]) {
                        case char_sender :
                            String str = new String(readBuf, 4, 1);
                            receivedTV.setText(" Character = " + str);
                            break;
                        case sinyal_yakalama_ayarlari :
                            if(readBuf[4] == 1) {
                                receivedTV.setText("Ayarlar Gönderildi");
                            }
                            break;
                        case adc_data_islemi :
                            String readMessage = new String (readBuf,0,msg.arg1);
                            Log.d(TAG,"adc_data_islemi: "+ readMessage);

                            adetMA = Bluetooth.unsignedToBytes(readBuf[5])+Bluetooth.unsignedToBytes(readBuf[6])*256;
                            DrawGraphics.grafikCiz(adetMA,readBuf);
                            recOrCur = true;
                            break;
                        case frekans_sec :
                            if(readBuf[4] == 1) {
                                String readMssage = new String (readBuf,0,msg.arg1);
                                Log.d(TAG,"frekans_sec: "+ readMssage);
                                Toast.makeText(getApplicationContext(),"Frekans ayarlandi", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case adc_gain_set :
                            if(readBuf[4] == 1) {
                                String readMssage = new String (readBuf,0,msg.arg1);
                                Log.d(TAG,"frekans_sec: "+ readMssage);
                                Toast.makeText(getApplicationContext(),"Kuvvetlendirme Ayarlandi", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }catch (Exception e) {
                    Log.e(TAG,"MESSAGE_READ: "+ e.getMessage());
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        Bluetooth.setHandler(handler);
        init();
    }

    private void init() {
        bConnect = (Button)findViewById(R.id.bConnect);
        bConfig = (Button)findViewById(R.id.bConfig);
        bDrawGraphics = (Button) findViewById(R.id.bDrawGraphics);
        bSettingReset = (Button)findViewById(R.id.bSettingReset);
        cbLed1 = (CheckBox)findViewById(R.id.cbLed1);
        cbLed2 = (CheckBox)findViewById(R.id.cbLed2);
        receivedTV = (TextView)findViewById(R.id.receivedTV);
        sendPac = new ArrayList<>();

        bConnect.setOnClickListener(this);
        bConfig.setOnClickListener(this);
        bDrawGraphics.setOnClickListener(this);
        bSettingReset.setOnClickListener(this);
        cbLed1.setOnClickListener(this);
        cbLed2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bConnect:
                Intent intentBT = new Intent(this, Bluetooth.class);
                startActivity(intentBT);
                break;
            case R.id.bConfig:
                Intent configIntent = new Intent(this, SystemConfig.class);
                startActivity(configIntent);
                break;
            case R.id.bDrawGraphics:
                Intent drawIntent = new Intent(this, DrawGraphics.class);
                startActivity(drawIntent);
                break;
            case R.id.bSettingReset:
                Intent resetIntent = new Intent(this, SettingReset.class);
                startActivity(resetIntent);
                break;
            case R.id.cbLed1:
                if(cbLed1.isChecked()) {
                    sendPac.clear();
                    sendPac.add((byte)0xff);
                    sendPac.add((byte)0);
                    sendPac.add((byte)2);
                    sendPac.add((byte)led_islemi);
                    sendPac.add((byte)1);
                    sendPacFunc(2);
                }else {
                    sendPac.clear();
                    sendPac.add((byte)0xff);
                    sendPac.add((byte)0);
                    sendPac.add((byte)2);
                    sendPac.add((byte)led_islemi);
                    sendPac.add((byte)0);
                    sendPacFunc(2);
                }
                break;
            case R.id.cbLed2:
                if(cbLed2.isChecked()) {
                    sendPac.clear();
                    sendPac.add((byte)0xff);
                    sendPac.add((byte)0);
                    sendPac.add((byte)2);
                    sendPac.add((byte)led_islemi);
                    sendPac.add((byte)2);
                    sendPacFunc(2);
                }else {
                    sendPac.clear();
                    sendPac.add((byte)0xff);
                    sendPac.add((byte)0);
                    sendPac.add((byte)2);
                    sendPac.add((byte)led_islemi);
                    sendPac.add((byte)0);
                    sendPacFunc(2);
                }
                break;
        }
    }

    public static void sendPacFunc(int adetMA) {
        byte[] sendToWrite = new byte[sendPac.size()];
        for(int i=0; i<sendPac.size();i++) {
            sendToWrite[i] = sendPac.get(i);
        }
        try {
            Bluetooth.mConnectedThread.write(sendToWrite);
        }catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity: onDestroy: called.");
    }

}
