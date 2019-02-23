package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.NoSuchElementException;
import java.util.TimerTask;


public class MyView extends View {

    private Paint back_paint;
    private Paint data_paint;
    private Path v_path;
    private Path h_path;
    private Path d_path;
    private DataStorage data;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        back_paint = new Paint();
        data_paint = new Paint();

        v_path = new Path();
        h_path = new Path();
        d_path = new Path();

        data=new DataStorage(40);

        back_paint.setAntiAlias(true);          //抗锯齿
        back_paint.setColor(getResources().getColor(R.color.string_big, null));//画笔颜色
        back_paint.setStyle(Paint.Style.STROKE);
        back_paint.setStrokeWidth(3);           //画笔粗细

        data_paint.setAntiAlias(true);          //抗锯齿
        data_paint.setStyle(Paint.Style.STROKE);
        data_paint.setStrokeWidth(3);
        data_paint.setColor(Color.BLACK);

        for(int i=0;i<data.getCapacity();i++)
            data.append((int)((Math.random()-0.5)*1000));

        java.util.Timer timer = new java.util.Timer(true);

        TimerTask task = new TimerTask() {
            public void run() {
                data.append((int)((Math.random()-0.5)*1000));

            }
        };
        timer.schedule(task, 0, 500);
    }


    //重写该方法，在这里绘图
    @Override
    protected void onDraw(Canvas canvas) {
        int width = this.getWidth();
        int height = this.getHeight();
        int zero_y=height/2;
        d_path.reset();//数据
        v_path.reset();//竖直线
        h_path.reset();//水平线

        for (int i = 0; i < width; i += 10) {
            v_path.moveTo(i, 0);
            v_path.lineTo(i, height);
        }
        for (int i = 0; i < height; i += 10) {
            h_path.moveTo(0, i);
            h_path.lineTo(width, i);
        }
        d_path.moveTo(0,zero_y);
        data.rewind();
        for(int i=0;i<Math.min(data.getLen(),width/10);i++){
            d_path.lineTo(i*10,zero_y-data.next());
        }

        canvas.drawPath(v_path, back_paint);//竖直线
        canvas.drawPath(h_path, back_paint);//水平线
        canvas.drawPath(d_path, data_paint);//数据

        super.onDraw(canvas);
        this.invalidate();
    }

    class DataStorage {
        private int[] data;
        private int capacity=40;
        private int len;
        private int p_next;
        private int p_start;
        private int p_data;

        DataStorage() {
            init();
            data = new int[capacity];
        }
        DataStorage(int capacity) {
            init();
            this.capacity=capacity;
            data = new int[capacity];
        }
        void init(){
            capacity = 500;
            len = 0;
            p_start = 0;
            p_data = p_start;
            p_next = 0;
        }
        void append(int d) {
            if (p_next == capacity) p_next = 0;
            if (p_start == capacity) p_start = 0;
            if (p_next == p_start &&len!=0) p_start++;
            else len += 1;
            data[p_next] = d;
            p_next += 1;
        }

        int delete() {
            if (len == 0) throw  new NoSuchElementException();
            else len -= 1;
            int tmp = data[p_start];
            p_start += 1;
            if (p_start == capacity) p_start = 0;
            return tmp;
        }

        void rewind() {
            p_data = p_start;
        }
        int next(){
            if (p_data==capacity)p_data=0;
            return data[p_data++];
        }
        int getCapacity(){
            return capacity;
        }
        int getLen(){
            return len;
        }
    }
}
