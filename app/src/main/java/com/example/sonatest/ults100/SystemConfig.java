package com.example.sonatest.ults100;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class SystemConfig extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "sametsMessage";

    private int SBVariableGainValue;
    public static int bLength;

    public static int gStart, gFinish;
    public static float soundVelocity;

    private EditText ETKapiBaslangic;
    private EditText ETKapiBitis;
    private EditText ETSVelocity;
    private EditText ETBufferLength;
    private EditText ETDarbeSayisi;
    private Spinner SPulseFreq;
    private Spinner SRectifier;
    private Spinner STrigger;
    private Spinner SPulseOutChannel;
    private Spinner SReadChannelSel;
    private Spinner SChannelSet;
    private Spinner SFrequncySet;
    private Spinner SLNPGain;
    private Spinner SChannelBitGain;
    private Spinner SUltrasonikYontem;
    private SeekBar SBVariableGain;
    private Button bSetLength;
    private TextView TVSeekBar;

    private ArrayAdapter SPulseFreqAdapter;
    private ArrayAdapter SRectifierAdapter;
    private ArrayAdapter STriggerAdapter;
    private ArrayAdapter SPulseOutChannelAdapter;
    private ArrayAdapter SReadChannelSelAdapter;
    private ArrayAdapter SChannelSetAdapter;
    private ArrayAdapter SFrequncySetAdapter;
    private ArrayAdapter SLNPGainAdapter;
    private ArrayAdapter SChannelBitGainAdapter;
    private ArrayAdapter SUltrasonikYontemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_config);
        init();

        SharedPreferences sharedPref = getSharedPreferences("SettingInfo",Context.MODE_PRIVATE);

        ETKapiBaslangic.setText(sharedPref.getString("kETKapiBaslangic","0"));
        ETKapiBitis.setText(sharedPref.getString("kETKapiBitis","0"));
        ETSVelocity.setText(sharedPref.getString("kETSVelocity","6300"));
        ETBufferLength.setText(sharedPref.getString("kETBufferLength","2000"));
        ETDarbeSayisi.setText(sharedPref.getString("kETDarbeSayisi","5"));
        SPulseFreq.setSelection(sharedPref.getInt("kSPulseFreq",1));
        SRectifier.setSelection(sharedPref.getInt("kSRectifier",3));
        STrigger.setSelection(sharedPref.getInt("kSTrigger",0));
        SPulseOutChannel.setSelection(sharedPref.getInt("kSPulseOutChannel",1));
        SReadChannelSel.setSelection(sharedPref.getInt("kSReadChannelSel",1));
        SChannelSet.setSelection(sharedPref.getInt("kSChannelSet",1));
        SFrequncySet.setSelection(sharedPref.getInt("kSFrequncySet",0));
        SLNPGain.setSelection(sharedPref.getInt("kSLNPGain",4));
        SChannelBitGain.setSelection(sharedPref.getInt("kSChannelBitGain",0));
        SUltrasonikYontem.setSelection(sharedPref.getInt("kSUltrasonikYontem",0));
        SBVariableGain.setProgress(sharedPref.getInt("kSBVariableGain",0));

        Log.d(TAG,"SystemConfig: onCreate: All Data has been loaded again");
    }

    private void init() {

        ETBufferLength = (EditText)findViewById(R.id.ETBufferLength);
        ETDarbeSayisi = (EditText)findViewById(R.id.ETDarbeSayisi);
        ETKapiBaslangic = (EditText)findViewById(R.id.ETKapiBaslangic);
        ETKapiBitis = (EditText)findViewById(R.id.ETKapiBitis);
        ETSVelocity = (EditText)findViewById(R.id.ETSVelocity);

        SPulseFreq = (Spinner) findViewById(R.id.SPulseFreq);
        SPulseFreqAdapter = ArrayAdapter.createFromResource(this,R.array.SPulseFreq_array,android.R.layout.simple_spinner_item);
        SPulseFreqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPulseFreq.setAdapter(SPulseFreqAdapter);

        SRectifier = (Spinner) findViewById(R.id.SRectifier);
        SRectifierAdapter = ArrayAdapter.createFromResource(this,R.array.SRectifier_array,android.R.layout.simple_spinner_item);
        SRectifierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SRectifier.setAdapter(SRectifierAdapter);

        STrigger = (Spinner) findViewById(R.id.STrigger);
        STriggerAdapter = ArrayAdapter.createFromResource(this,R.array.STrigger_array,android.R.layout.simple_spinner_item);
        STriggerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        STrigger.setAdapter(STriggerAdapter);

        SPulseOutChannel = (Spinner) findViewById(R.id.SPulseOutChannel);
        SPulseOutChannelAdapter = ArrayAdapter.createFromResource(this,R.array.SPulseOutChannel_array,android.R.layout.simple_spinner_item);
        SPulseOutChannelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPulseOutChannel.setAdapter(SPulseOutChannelAdapter);

        SReadChannelSel = (Spinner) findViewById(R.id.SReadChannelSel);
        SReadChannelSelAdapter = ArrayAdapter.createFromResource(this,R.array.SReadChannelSel_array,android.R.layout.simple_spinner_item);
        SReadChannelSelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SReadChannelSel.setAdapter(SReadChannelSelAdapter);

        SChannelSet = (Spinner) findViewById(R.id.SChannelSet);
        SChannelSetAdapter = ArrayAdapter.createFromResource(this,R.array.SChannelSet_array,android.R.layout.simple_spinner_item);
        SChannelSetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SChannelSet.setAdapter(SChannelSetAdapter);

        SFrequncySet = (Spinner) findViewById(R.id.SFrequncySet);
        SFrequncySetAdapter = ArrayAdapter.createFromResource(this,R.array.SFrequncySet_array,android.R.layout.simple_spinner_item);
        SFrequncySetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SFrequncySet.setAdapter(SFrequncySetAdapter);

        SLNPGain = (Spinner) findViewById(R.id.SLNPGain);
        SLNPGainAdapter = ArrayAdapter.createFromResource(this,R.array.SLNPGain_array,android.R.layout.simple_spinner_item);
        SLNPGainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SLNPGain.setAdapter(SLNPGainAdapter);

        SChannelBitGain = (Spinner) findViewById(R.id.SChannelBitGain);
        SChannelBitGainAdapter = ArrayAdapter.createFromResource(this,R.array.SChannelBitGain_array,android.R.layout.simple_spinner_item);
        SChannelBitGainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SChannelBitGain.setAdapter(SChannelBitGainAdapter);

        SUltrasonikYontem = (Spinner) findViewById(R.id.SUltrasonikYontem);
        SUltrasonikYontemAdapter = ArrayAdapter.createFromResource(this,R.array.SUltrasonikYontem_array,android.R.layout.simple_spinner_item);
        SUltrasonikYontemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SUltrasonikYontem.setAdapter(SUltrasonikYontemAdapter);

        TVSeekBar = (TextView)findViewById(R.id.TVSeekBar);
        SBVariableGain = (SeekBar) findViewById(R.id.SBVariableGain);
        SBVariableGain.setOnSeekBarChangeListener(this);

        bSetLength = (Button)findViewById(R.id.bSetLength);
        bSetLength.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bSetLength:

                gStart = Integer.parseInt(ETKapiBaslangic.getText().toString());
                gFinish = Integer.parseInt(ETKapiBitis.getText().toString());
                soundVelocity = Integer.parseInt(ETSVelocity.getText().toString());

                String readBufferLength = ETBufferLength.getText().toString();
                bLength = Integer.parseInt(readBufferLength);

                MainActivity.sendPac.clear();
                MainActivity.sendPac.add((byte)0xff);
                MainActivity.sendPac.add((byte)0);
                MainActivity.sendPac.add((byte)26);
                MainActivity.sendPac.add((byte)MainActivity.sinyal_yakalama_ayarlari);
                MainActivity.sendPac.add((byte)1);
                MainActivity.sendPac.add((byte)(bLength%256));                                          // Buffer Boyutu LSB
                MainActivity.sendPac.add((byte)(bLength/256));                                          // Buffer Boyutu MSB
                MainActivity.sendPac.add((byte)0);                                                      // Kapi Baslangic LSB
                MainActivity.sendPac.add((byte)0);                                                      // Kapi Baslangic MSB
                MainActivity.sendPac.add((byte)0);                                                      // Kapi Bitis LSB
                MainActivity.sendPac.add((byte)0);                                                      // Kapi Bitis MSB
                MainActivity.sendPac.add((byte)1);                                                      // RAW Data 1 hep simdilik
                MainActivity.sendPac.add((byte)SRectifier.getSelectedItemPosition());                   // NO Rectifier
                MainActivity.sendPac.add((byte)0);                                                      // No Trigger
                MainActivity.sendPac.add((byte)SFrequncySet.getSelectedItemPosition());                 // Frekans sec 0 = 100 MHZ
                MainActivity.sendPac.add((byte)SChannelSet.getSelectedItemPosition());                  // Kanal Sec Sadece ADC Okuma
                MainActivity.sendPac.add((byte)0);
                MainActivity.sendPac.add((byte)0);                                                      // PRF 0
                MainActivity.sendPac.add((byte)0);                                                      // FIR Filtre Yok
                MainActivity.sendPac.add((byte)0);
                MainActivity.sendPac.add((byte)SLNPGain.getSelectedItemPosition());                     // LNP kuvvetlendirme miktari
                MainActivity.sendPac.add((byte)SBVariableGainValue);                                    // VGA Gain
                MainActivity.sendPac.add((byte)SPulseFreq.getSelectedItemPosition());                   // Darbe Frekansı 5 MHZ
                MainActivity.sendPac.add((byte)Integer.parseInt(ETDarbeSayisi.getText().toString()));   // Gonderilecek Darbe sayisi
                MainActivity.sendPac.add((byte)SPulseOutChannel.getSelectedItemPosition());             // Darbe Gonderilecek kanal B
                MainActivity.sendPac.add((byte)0);                                                      // HV Degeri
                MainActivity.sendPac.add((byte)SReadChannelSel.getSelectedItemPosition());              // ADC Okunan Kanal 1 olunca B
                MainActivity.sendPac.add((byte)SChannelBitGain.getSelectedItemPosition());
                MainActivity.sendPac.add((byte)SUltrasonikYontem.getSelectedItemPosition());            // TTU Dual Element

                MainActivity.sendPacFunc(26);

                break;
        }

    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedPref = getSharedPreferences("SettingInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("kETKapiBaslangic", ETKapiBaslangic.getText().toString());
        editor.putString("kETKapiBitis", ETKapiBitis.getText().toString());
        editor.putString("kETSVelocity", ETSVelocity.getText().toString());
        editor.putString("kETBufferLength", ETBufferLength.getText().toString());
        editor.putString("kETDarbeSayisi", ETDarbeSayisi.getText().toString());
        editor.putInt("kSPulseFreq",SPulseFreq.getSelectedItemPosition());
        editor.putInt("kSRectifier",SRectifier.getSelectedItemPosition());
        editor.putInt("kSTrigger",STrigger.getSelectedItemPosition());
        editor.putInt("kSPulseOutChannel",SPulseOutChannel.getSelectedItemPosition());
        editor.putInt("kSReadChannelSel",SReadChannelSel.getSelectedItemPosition());
        editor.putInt("kSChannelSet",SChannelSet.getSelectedItemPosition());
        editor.putInt("kSFrequncySet",SFrequncySet.getSelectedItemPosition());
        editor.putInt("kSLNPGain",SLNPGain.getSelectedItemPosition());
        editor.putInt("kSChannelBitGain",SChannelBitGain.getSelectedItemPosition());
        editor.putInt("kSUltrasonikYontem",SUltrasonikYontem.getSelectedItemPosition());
        editor.putInt("kSBVariableGain",SBVariableGain.getProgress());
        editor.apply();

        Log.d(TAG,"SystemConfig: onDestroy: All Data has been saved");

        super.onDestroy();
        Log.d(TAG, "SystemConfig: onDestroy: called.");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        SBVariableGainValue = progress;
        TVSeekBar.setText("Variable Gain:            " + SBVariableGainValue + "/" + SBVariableGain.getMax());
    }       //seekbar

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }                                    //seekbar

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }                                     //seekbar
}
