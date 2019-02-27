package com.example.administrator.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.administrator.myapplication.util.Record;

import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;

public class HistoryFragment extends Fragment implements AdapterView.OnItemClickListener {
    private Context mContext;
    private LinkedList<RecordItem> mData;
    Record record;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_history, container, false);
        ListView list = view.findViewById(R.id.list_record);
        mData = new LinkedList<RecordItem>();
        for (int i = 0; i < 20; i++) mData.add(new RecordItem(new Date().getTime(), i));
        RecordItemAdapter mAdapter = new RecordItemAdapter(mData, view.getContext());
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(this);
        mContext = view.getContext();
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent start_intent = new Intent();
        start_intent.setClass(Objects.requireNonNull(getActivity()).getApplicationContext(), HistoryActivity.class);
        start_intent.putExtra("record", record);
        startActivity(start_intent);
    }
}
