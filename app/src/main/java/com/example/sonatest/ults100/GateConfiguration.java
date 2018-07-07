package com.example.sonatest.ults100;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GateConfiguration extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private Button bLeft, bRight, bUp, bDown, bMeasure;
    private SeekBar sBGateWidth;
    private int x1, x2, y, peak2y,  peak1y;
    public static int peak2x, peak1x;
    private DataPoint[] gDataPoints;
    private double [] xValues;
    public static LineGraphSeries<DataPoint> gSeries;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_gate_configuration, container, false);

        sBGateWidth = (SeekBar) rootView.findViewById(R.id.sBGateWidth);
        sBGateWidth.setOnSeekBarChangeListener(this);

        bDown = (Button)rootView.findViewById(R.id.bDown);
        bUp = (Button)rootView.findViewById(R.id.bUp);
        bRight = (Button)rootView.findViewById(R.id.bRight);
        bLeft = (Button)rootView.findViewById(R.id.bLeft);
        bMeasure = (Button)rootView.findViewById(R.id.bMeasure);

        bDown.setOnClickListener(this);
        bUp.setOnClickListener(this);
        bLeft.setOnClickListener(this);
        bRight.setOnClickListener(this);
        bMeasure.setOnClickListener(this);

        x1 = 0;
        x2 = 300;
        y = 200;

        xValues = new double[x2-x1];
        gDataPoints = new DataPoint[x2-x1];

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bLeft:
                DrawGraphics.graph.removeSeries(gSeries);

                if(x1>0) {
                    x1 -= 100;
                    x2 -= 100;
                }else {}

                for(int x = 0; x<(x2-x1); x++) {
                    xValues[x] = x1+x;
                }

                for(int i = 0; i<x2-x1; i++) {
                    gDataPoints[i] = new DataPoint(xValues[i], (double) y);
                }

                gSeries = new LineGraphSeries<>(gDataPoints);
                gSeries.setColor(Color.RED);
                gSeries.setThickness(10);
                DrawGraphics.graph.addSeries(gSeries);
                break;

            case R.id.bRight:
                DrawGraphics.graph.removeSeries(gSeries);

                if(x2<SystemConfig.bLength) {
                    x1 += 100;
                    x2 += 100;
                }else {}

                for(int x = 0; x<(x2-x1); x++) {
                    xValues[x] = x1+x;
                }

                for(int i = 0; i<x2-x1; i++) {
                    gDataPoints[i] = new DataPoint(xValues[i], (double) y);
                }

                gSeries = new LineGraphSeries<>(gDataPoints);
                gSeries.setColor(Color.RED);
                gSeries.setThickness(10);
                DrawGraphics.graph.addSeries(gSeries);
                break;

            case R.id.bUp:
                DrawGraphics.graph.removeSeries(gSeries);

                if(y<250) {
                    y = y + 10;
                }else{}

                for(int x = 0; x<(x2-x1); x++) {
                    xValues[x] = x1+x;
                }

                for(int i = 0; i<x2-x1; i++) {
                    gDataPoints[i] = new DataPoint(xValues[i], (double) y);
                }

                gSeries = new LineGraphSeries<>(gDataPoints);
                gSeries.setColor(Color.RED);
                gSeries.setThickness(10);
                DrawGraphics.graph.addSeries(gSeries);
                break;

            case R.id.bDown:
                DrawGraphics.graph.removeSeries(gSeries);

                if(y>0) {
                    y = y - 10;
                }else{}


                for(int x = 0; x<(x2-x1); x++) {
                    xValues[x] = x1+x;
                }

                for(int i = 0; i<x2-x1; i++) {
                    gDataPoints[i] = new DataPoint(xValues[i], (double) y);
                }

                gSeries = new LineGraphSeries<>(gDataPoints);
                gSeries.setColor(Color.RED);
                gSeries.setThickness(10);
                DrawGraphics.graph.addSeries(gSeries);
                break;

            case R.id.bMeasure:
                if(MainActivity.recOrCur) {
                    peak2y =  Bluetooth.unsignedToBytes(MainActivity.readBuf[x1+53]);
                    peak2x = x1;

                    for(int i=0; i<=x2-x1; i++) {
                        if(peak2y < Bluetooth.unsignedToBytes(MainActivity.readBuf[x1+53+i])) {
                            peak2y =  Bluetooth.unsignedToBytes(MainActivity.readBuf[x1 + 53 + i]);
                            peak2x = x1 + i;
                        }
                    }

                    peak1y =  Bluetooth.unsignedToBytes(MainActivity.readBuf[53]);
                    peak1x = 53;
                    for(int i=SystemConfig.gStart; i<=SystemConfig.gFinish; i++) {
                        if(peak1y < Bluetooth.unsignedToBytes(MainActivity.readBuf[53+i])) {
                            peak1y =  Bluetooth.unsignedToBytes(MainActivity.readBuf[53+i]);
                            peak1x = i;
                        }
                    }

                }
                else {
                    peak2y =  Bluetooth.unsignedToBytes(RecordDatabaseFragment.savedBuf[x1+53]);
                    peak2x = x1;

                    for(int i=0; i<=x2-x1; i++) {
                        if(peak2y < Bluetooth.unsignedToBytes(RecordDatabaseFragment.savedBuf[x1+53+i])) {
                            peak2y = Bluetooth.unsignedToBytes(RecordDatabaseFragment.savedBuf[x1 + 53 + i]);
                            peak2x = x1 + i;
                        }
                    }

                    peak1y =  Bluetooth.unsignedToBytes(RecordDatabaseFragment.savedBuf[53]);
                    peak1x = 53;
                    for(int i=SystemConfig.gStart; i<=SystemConfig.gFinish; i++) {
                        if(peak1y < Bluetooth.unsignedToBytes(RecordDatabaseFragment.savedBuf[53+i])) {
                            peak1y =  Bluetooth.unsignedToBytes(RecordDatabaseFragment.savedBuf[53+i]);
                            peak1x = i;
                        }
                    }
                }

                //Toast.makeText(getActivity(),"Gecen süre: " + (peak2x - peak1x) ,Toast.LENGTH_SHORT).show();

                float distance = SystemConfig.soundVelocity * (peak2x-peak1x) /200000;
                DrawGraphics.tVDistance.setText("Distance: " + distance + "mm");
                break;

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
