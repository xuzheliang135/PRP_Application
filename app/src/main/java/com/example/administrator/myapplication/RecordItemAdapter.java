package com.example.administrator.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RecordItemAdapter extends BaseAdapter {

    private List<RecordItem> mData;
    private Context mContext;

    public RecordItemAdapter(List<RecordItem> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.recoed_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.date = convertView.findViewById(R.id.date);
            viewHolder.beats_per_minute = convertView.findViewById(R.id.beats_per_minute);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.date.setText(mData.get(position).get_date());
        viewHolder.beats_per_minute.setText(mData.get(position).get_beats());
        return convertView;
    }

    private class ViewHolder {
        TextView date;
        TextView beats_per_minute;
    }

}