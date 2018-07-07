package com.example.sonatest.ults100;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class RecordDatabaseFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    DatabaseHandler dbHandler;

    private String nameToDelete;
    static byte[] savedBuf;
    static int adetDG;

    private Button bRec;
    private Button bDeleteRec;
    private ListView ListVRec;
    private ArrayAdapter ListVRecAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record_database,container,false);
        dbHandler = new DatabaseHandler(getActivity(),null,null,1);

        bRec = (Button) rootView.findViewById(R.id.bRec);
        bRec.setOnClickListener(this);
        bDeleteRec = (Button)rootView.findViewById(R.id.bDeleteRec);
        bDeleteRec.setOnClickListener(this);

        ListVRecAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dbHandler.getNameForListView());
        ListVRec = (ListView) rootView.findViewById(R.id.ListVRec);
        ListVRec.setAdapter(ListVRecAdapter);
        ListVRec.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRec:
                try {
                    if(MainActivity.readBuf[3] == MainActivity.adc_data_islemi ) {
                        dbHandler.setSignalData(MainActivity.readBuf);
                        dbHandler.addSignalToTable();
                        ListVRecAdapter.clear();
                        ListVRecAdapter.addAll(dbHandler.getNameForListView());
                    }
                }catch (Exception e) {
                    Toast.makeText(getActivity(),"Önce bir ölçüm yapınız", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bDeleteRec:
                if(nameToDelete != null) {
                    dbHandler.deleteSignal(nameToDelete);
                    Toast.makeText(getActivity(), nameToDelete + " tarihli kayıt silindi.",Toast.LENGTH_SHORT).show();
                    DrawGraphics.graph.removeAllSeries();
                    ListVRecAdapter.clear();
                    ListVRecAdapter.addAll(dbHandler.getNameForListView());
                }else {
                    Toast.makeText(getActivity(),"Önce listeden bir kayıt seçiniz.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.ListVRec :
                Toast.makeText(getActivity(), String.valueOf(parent.getItemAtPosition(position)) +
                        " tarihinde kaydedildi.",Toast.LENGTH_SHORT).show();

                nameToDelete = String.valueOf(parent.getItemAtPosition(position));

                savedBuf = dbHandler.databaseToGraph(String.valueOf(parent.getItemAtPosition(position)));
                if(savedBuf[3] == MainActivity.adc_data_islemi ) {
                    adetDG = Bluetooth.unsignedToBytes(savedBuf[5])+Bluetooth.unsignedToBytes(savedBuf[6])*256;
                    DrawGraphics.grafikCiz(adetDG,savedBuf);
                }
                MainActivity.recOrCur = false;
                break;
        }
    }
}
