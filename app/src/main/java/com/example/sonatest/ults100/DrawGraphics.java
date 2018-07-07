package com.example.sonatest.ults100;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class DrawGraphics extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "sametsMessage";

    private Button bDrawData;
    private Button bRecFragment;
    private Button bSamplingFreqFragment;
    private Button bGainFragment;
    private Button bGConfig;
    private Button bCalib;
    public static TextView tVDistance;

    FragmentManager fm;
    FragmentTransaction ft;

    public static GraphView graph;
    private static LineGraphSeries<DataPoint> series;

    static long startTime, finishTime1, finishTime2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_draw_graphics);

        fm = getFragmentManager();

        bDrawData = (Button)findViewById(R.id.bDrawData);
        bDrawData.setOnClickListener(this);
        bRecFragment = (Button)findViewById(R.id.bRecFragment);
        bRecFragment.setOnClickListener(this);
        bSamplingFreqFragment = (Button)findViewById(R.id.bSamplingFreqFragment);
        bSamplingFreqFragment.setOnClickListener(this);
        bGainFragment = (Button)findViewById(R.id.bGainFragment);
        bGainFragment.setOnClickListener(this);
        bGConfig = (Button)findViewById(R.id.bGConfig);
        bGConfig.setOnClickListener(this);
        bCalib = (Button)findViewById(R.id.bCalib);
        bCalib.setOnClickListener(this);

        tVDistance = (TextView)findViewById(R.id.tVDistance);
        tVDistance.setText("Distance:");

        graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(260);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(2000);
        graph.getViewport().setScalable(true);
        graph.getGridLabelRenderer().setGridColor(Color.YELLOW);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.YELLOW);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.YELLOW);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.YELLOW);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bDrawData:
                startTime =System.currentTimeMillis();
                MainActivity.sendPac.clear();
                MainActivity.sendPac.add((byte)0xff);
                MainActivity.sendPac.add((byte)0);
                MainActivity.sendPac.add((byte)3);
                MainActivity.sendPac.add((byte)MainActivity.adc_ornek_gonder);
                MainActivity.sendPac.add((byte)1);
                MainActivity.sendPac.add((byte)0);
                MainActivity.sendPacFunc(3);
            break;
            case R.id.bRecFragment:
                ft = fm.beginTransaction();
                RecordDatabaseFragment fRBD = (RecordDatabaseFragment) fm.findFragmentByTag("RecDBTag");
                if(fRBD == null) {  // not added
                    fRBD = new RecordDatabaseFragment();
                    ft.replace(R.id.fragment_container, fRBD, "RecDBTag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                } else {  // already added

                    ft.remove(fRBD);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                }
                ft.commit();
                break;
            case R.id.bSamplingFreqFragment:
                ft = fm.beginTransaction();
                SamplingFrequencyFragment fSF = (SamplingFrequencyFragment) fm.findFragmentByTag("SamFTag");
                if(fSF == null) {  // not added
                    fSF = new SamplingFrequencyFragment();
                    ft.replace(R.id.fragment_container, fSF, "SamFTag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                } else {  // already added

                    ft.remove(fSF);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                }
                ft.commit();
                break;
            case R.id.bGainFragment:
                ft = fm.beginTransaction();
                GainFragment fG = (GainFragment) fm.findFragmentByTag("GainTag");
                if(fG == null) {  // not added
                    fG = new GainFragment();
                    ft.replace(R.id.fragment_container, fG, "GainTag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                } else {  // already added

                    ft.remove(fG);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                }
                ft.commit();
                break;

            case R.id.bGConfig:
                ft = fm.beginTransaction();
                GateConfiguration f = (GateConfiguration) fm.findFragmentByTag("gateTag");
                if(f == null) {  // not added
                    graph.removeSeries(GateConfiguration.gSeries);
                    f = new GateConfiguration();
                    ft.replace(R.id.fragment_container_GC, f, "gateTag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                } else {  // already added
                    ft.remove(f);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                }
                ft.commit();
                break;

            case R.id.bCalib:
                ft = fm.beginTransaction();
                ProbeCalibration fPC = (ProbeCalibration) fm.findFragmentByTag("calibTag");
                if(fPC == null) {  // not added
                    fPC = new ProbeCalibration();
                    ft.replace(R.id.fragment_container, fPC, "calibTag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                } else {  // already added
                    ft.remove(fPC);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                }
                ft.commit();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DrawGraphics: onDestroy: called.");
    }

    public static void grafikCiz(int adetDG, byte[] tempBuf) {
        finishTime1 =System.currentTimeMillis();
        DataPoint[] points = new DataPoint[adetDG];
        for(int i=0; i<adetDG;i++) {
            points[i] = new DataPoint(i,Bluetooth.unsignedToBytes(tempBuf[i+53]));

        }
        graph.removeAllSeries();
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(adetDG);
        series = new LineGraphSeries<>(points);
        series.setColor(Color.GREEN);
        series.setThickness(5);
        DrawGraphics.graph.addSeries(series);
        finishTime2 =System.currentTimeMillis();
    }


}
