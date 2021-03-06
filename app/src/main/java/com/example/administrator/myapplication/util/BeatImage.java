package com.example.administrator.myapplication.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import com.example.administrator.myapplication.Config;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class BeatImage {
    private volatile LinkedList<Path> paths;
    private volatile LinkedList<Paint> paints;
    private Record record;
    private int width;
    private int height;
    private int startIndex;
    private int showLength = 70;

    private SQLUtil sqlUtil;

    public BeatImage(int width, int height, Context mContext) {
        this.height = height;
        this.width = width;
        sqlUtil = new SQLUtil(mContext);
        init();
    }

    private void init() {
        this.record = new Record();
        paths = new LinkedList<>();
        paints = new LinkedList<>();
        Paint back_paint = new Paint();
        Paint data_paint = new Paint();

        back_paint.setAntiAlias(true);          //抗锯齿
        back_paint.setColor(Color.argb(255, 253, 226, 182));//画笔颜色
        back_paint.setStyle(Paint.Style.STROKE);
        back_paint.setStrokeWidth(3);           //画笔粗细

        data_paint.setAntiAlias(true);          //抗锯齿
        data_paint.setStyle(Paint.Style.STROKE);
        data_paint.setStrokeWidth(3);
        data_paint.setColor(Color.BLACK);

        paints.add(back_paint);
        paints.add(back_paint);
        for (int i = 0; i < Config.channelNumber; i++) {
            paints.add(data_paint);
        }
        addBackPath();
        addDataPath();
    }

    void save() {
        sqlUtil.record(record);
    }

    public LinkedList<Paint> getPaints() {
        return paints;
    }

    private synchronized void addBackPath() {
        Path h_path = new Path();
        Path v_path = new Path();
        for (int i = 0; i < width; i += 10) {
            v_path.moveTo(i, 0);
            v_path.lineTo(i, height);
        }
        for (int i = 0; i < height; i += 10) {
            h_path.moveTo(0, i);
            h_path.lineTo(width, i);
        }
        paths.addFirst(h_path);
        paths.addFirst(v_path);
    }

    private synchronized void onSizeChanged() {
        showLength = width / Config.pointGap;
        updateBackPath();
        updateDataPath();
    }

    private synchronized void updateBackPath() {
        paths.remove();
        paths.remove();
        addBackPath();
    }

    public synchronized void setWidth(int width) {
        setSize(width, height);
        updateBackPath();
    }

    public synchronized void setHeight(int height) {
        setSize(width, height);
        updateBackPath();
    }

    public synchronized void setSize(int width, int height) {
        this.height = height;
        this.width = width;
        onSizeChanged();
    }

    public synchronized void setStartIndex(int startIndex) {
        int length = record.getPoints(0).size();
        startIndex = startIndex < length ? startIndex : length;
        this.startIndex = startIndex > 0 ? startIndex : 0;
        updateDataPath();
    }

    public synchronized int getStartIndex() {
        return startIndex;
    }

    public synchronized void setRecord(Record record) {
        this.record = record;
        updateDataPath();
    }

    public synchronized LinkedList<Path> getPaths() {
        return paths;
    }

    private synchronized void updateDataPath() {
        for (int i = 0; i < Config.channelNumber; i++) paths.removeLast();
        addDataPath();
    }

    private synchronized void addDataPath() {
        final int channelHeight = height / Config.channelNumber;
        for (int i = 0; i < Config.channelNumber; i++) {
            int signalMAX;
            List<Integer> subPoints = new LinkedList<>();
            LinkedList<Integer> points = record.getPoints(i);
            int realShowLength = Math.min(points.size() - startIndex, showLength);
            if (realShowLength > 0) subPoints = points.subList(startIndex, startIndex + realShowLength);
            if (realShowLength > 0) {
                signalMAX = Math.max(Math.abs(Collections.max(subPoints)), Math.abs(Collections.min(subPoints)));
                signalMAX = Math.max(Config.leastSignalMax, signalMAX);
            } else {
                signalMAX = 100;
            }
            int zero_y = channelHeight / 2 + i * channelHeight;
            Path dataPath = new Path();
            dataPath.moveTo(0, zero_y);
            for (int j = 0; j < realShowLength; j++) {
                int value = subPoints.get(j);
                dataPath.lineTo(j * Config.pointGap, zero_y - (value * channelHeight / 2 / signalMAX));
            }
            paths.add(dataPath);
        }
    }

    synchronized void append(int[] value) {//todo: limit the value:too large value can break the drawing function
        for (int i = 0; i < Config.channelNumber; i++) record.append_points(i, value[i]);
        if (record.getPoints(0).size() > showLength) startIndex += 1;
        Log.d("my_debug", "recordSize" + record.getPoints(0).size());
        updateDataPath();
    }

    public void clearAll() {
        for (int i = 0; i < Config.channelNumber; i++) record.getPoints(i).clear();
        init();
        startIndex = 0;
    }
}
