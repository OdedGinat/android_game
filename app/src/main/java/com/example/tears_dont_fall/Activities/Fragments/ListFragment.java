package com.example.tears_dont_fall.Activities.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tears_dont_fall.Activities.Interfaces.Callback_List;
import com.example.tears_dont_fall.DataManager;
import com.example.tears_dont_fall.Model.Record;
import com.example.tears_dont_fall.R;

public class ListFragment extends Fragment {

    private ListView list_LV;
    private Callback_List callback_list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initListView();

        list_LV.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                double lat = DataManager.getInstance().getTopRecords().get(position).getLatitude();
                double lon = DataManager.getInstance().getTopRecords().get(position).getLongitude();
                String namePlayer = DataManager.getInstance().getTopRecords().get(position).getName();
                callback_list.setMapLocation(lat,lon,namePlayer);
            }
        });

        return view;
    }

    private void initListView() {
        if(DataManager.getInstance().getTopRecords() != null){
            ArrayAdapter<Record> adapter = new ArrayAdapter<Record>(getActivity(), R.layout.item_list, DataManager.getInstance().getTopRecords());
            list_LV.setAdapter(adapter);
        }
    }


    private void findViews(View view){
        this.list_LV = view.findViewById(R.id.list_LV);
    }

    public void setCallback_list(Callback_List callback_list){
        this.callback_list = callback_list;
    }
}