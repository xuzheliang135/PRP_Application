package com.example.administrator.myapplication.util;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.LinkedList;

public class BeatImage {
    private volatile LinkedList<Path> paths;
    private volatile LinkedList<Paint> paints;
    private Record record;
    private int width;
    private int height;
    private int startIndex;
    int showLenght = 40;

    public BeatImage(int width, int height, Record record) {
        this.height = height;
        this.width = width;
        this.record = record;
        init();
    }

    private void init() {
        paths = new LinkedList<Path>();
        paints = new LinkedList<Paint>();
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
        paints.add(data_paint);
        addBackPath();
        addDataPath();
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

    private synchronized void updateBackPath() {
        paths.remove();
        paths.remove();
        addBackPath();
    }

    public synchronized void setWidth(int width) {
        this.width = width;
        updateBackPath();
    }

    public synchronized void setHeight(int height) {
        this.height = height;
        updateBackPath();
    }

    public synchronized void setSize(int width, int height) {
        this.height = height;
        this.width = width;
        updateBackPath();
    }

    public synchronized void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }


    public Record getRecord() {
        return record;
    }

    public synchronized void setRecord(Record record) {
        this.record = record;
        updateDataPath();
    }

    public LinkedList<Path> getPaths() {
        return paths;
    }

    private synchronized void updateDataPath() {
        paths.removeLast();
        addDataPath();
    }

    private synchronized void addDataPath() {
        LinkedList<Integer> points = record.getPoints();
        int zero_y = height / 2;
        Path d_path = new Path();
        d_path.moveTo(0, zero_y);
        for (int i = 0; i < Math.min(points.size() - startIndex, showLenght); i++) {
            d_path.lineTo(i * 10, zero_y - points.get(startIndex + i));
        }
        paths.add(d_path);
    }

    public synchronized void append(int value) {
        record.append_points(value);
        if (record.getPoints().size() > showLenght) startIndex += 1;
        updateDataPath();
    }

}
