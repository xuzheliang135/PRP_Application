package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.administrator.myapplication.util.SQLUtil;

import java.util.LinkedList;
import java.util.Objects;

public class HistoryFragment extends Fragment implements AdapterView.OnItemClickListener {
    private Context mContext;
    private HistoryItemAdapter historyItemAdapter;
    private LinkedList<Long> items;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_history, container, false);
        ListView list = view.findViewById(R.id.list_record);
        mContext = view.getContext();
        items = new SQLUtil(mContext).get_date_list();
        historyItemAdapter = new HistoryItemAdapter(items, mContext);
        list.setAdapter(historyItemAdapter);
        list.setOnItemClickListener(this);
        return view;
    }

    void refreshList() {
        items = new SQLUtil(mContext).get_date_list();
        historyItemAdapter.setData(items);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent start_intent = new Intent();
        start_intent.setClass(Objects.requireNonNull(getActivity()).getApplicationContext(), HistoryActivity.class);
        start_intent.putExtra("date", items.get(position));
        startActivity(start_intent);
    }
}
