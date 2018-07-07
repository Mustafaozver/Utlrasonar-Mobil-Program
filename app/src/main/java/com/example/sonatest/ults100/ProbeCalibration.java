package com.example.sonatest.ults100;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProbeCalibration extends Fragment implements View.OnClickListener {

    private Button bCDistance;
    private EditText eTCDistance;
    private float disMaterial;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_probe_calibration, container, false);

        bCDistance = (Button)rootView.findViewById(R.id.bCDistance);
        bCDistance.setOnClickListener(this);

        eTCDistance = (EditText)rootView.findViewById(R.id.eTCDistance);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bCDistance:
                disMaterial = Float.parseFloat(eTCDistance.getText().toString());
                SystemConfig.soundVelocity = 2 * 100000 * disMaterial / ((GateConfiguration.peak2x-GateConfiguration.peak1x));



                Toast.makeText(getActivity(),"Ses Hızı: " + SystemConfig.soundVelocity ,Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
