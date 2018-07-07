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

public class SamplingFrequencyFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayAdapter lVFrekansSecAdapter;

    private ListView lVFrekansSec;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sampling_frequency_set, container, false);

        lVFrekansSecAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.SFrequncySet_array,android.R.layout.simple_spinner_item);
        lVFrekansSec = (ListView) rootView.findViewById(R.id.lVFrekansSec);
        lVFrekansSec.setAdapter(lVFrekansSecAdapter);
        lVFrekansSec.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lVFrekansSec :
                MainActivity.sendPac.clear();
                MainActivity.sendPac.add((byte)0xff);
                MainActivity.sendPac.add((byte)0);
                MainActivity.sendPac.add((byte)2);
                MainActivity.sendPac.add((byte)MainActivity.frekans_sec);
                MainActivity.sendPac.add((byte)position);
                MainActivity.sendPacFunc(MainActivity.sendPac.size()-3);
                break;
        }
    }
}
