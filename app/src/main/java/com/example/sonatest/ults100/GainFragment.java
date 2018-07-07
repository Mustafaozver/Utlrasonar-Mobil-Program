package com.example.sonatest.ults100;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class GainFragment extends Fragment implements AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener {

    private ArrayAdapter lVLNPGainAdapter;

    private int SBVarGainValue;

    private ListView lVLNPGain;
    private TextView TVSB;
    private SeekBar SBVarGain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gain, container, false);

        lVLNPGainAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.SLNPGain_array,android.R.layout.simple_spinner_item);
        lVLNPGain = (ListView)rootView.findViewById(R.id.lVLNPGain);
        lVLNPGain.setAdapter(lVLNPGainAdapter);
        lVLNPGain.setOnItemClickListener(this);

        TVSB = (TextView) rootView.findViewById(R.id.TVSB);
        SBVarGain = (SeekBar) rootView.findViewById(R.id.SBVarGain);
        SBVarGain.setOnSeekBarChangeListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lVLNPGain :
                MainActivity.sendPac.clear();
                MainActivity.sendPac.add((byte)0xff);
                MainActivity.sendPac.add((byte)0);
                MainActivity.sendPac.add((byte)3);
                MainActivity.sendPac.add((byte)MainActivity.adc_gain_set);
                MainActivity.sendPac.add((byte)SBVarGainValue); //trackbar value
                MainActivity.sendPac.add((byte)position);
                MainActivity.sendPacFunc(MainActivity.sendPac.size()-3);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        SBVarGainValue = progress;
        TVSB.setText("Variable Gain:            " + SBVarGainValue + "/" + SBVarGain.getMax());
    }       //seekbar

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }                                    //seekbar

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
