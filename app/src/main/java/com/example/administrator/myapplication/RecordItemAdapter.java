package com.example.administrator.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.administrator.myapplication.util.Record;

import java.util.LinkedList;
import java.util.List;

public class RecordItemAdapter extends BaseAdapter {

    private List<Long> data;
    private Context mContext;

    public RecordItemAdapter(List<Long> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(LinkedList<Long> date) {
        data = date;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.recoed_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.date = convertView.findViewById(R.id.date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.date.setText(Record.format(data.get(position)));
        return convertView;
    }

    private class ViewHolder {
        TextView date;
    }

}