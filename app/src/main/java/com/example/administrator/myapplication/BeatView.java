package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import com.example.administrator.myapplication.util.BeatImage;
import com.example.administrator.myapplication.util.Record;

import java.util.LinkedList;


public class BeatView extends View {

    private BeatImage beatImage;
    private int width, height;

    public BeatView(Context context) {
        super(context);
        init();
    }

    public BeatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BeatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        beatImage = new BeatImage(100, 100, new Record());
    }

    public BeatImage getModel() {
        return beatImage;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        if (this.getWidth() != width || this.getHeight() != height) {
            width = this.getWidth();
            height = this.getHeight();
            beatImage.setSize(width, height);
        }
        LinkedList<Path> paths = beatImage.getPaths();
        LinkedList<Paint> paints = beatImage.getPaints();
        for (int i = 0; i < Math.min(paints.size(), paths.size()); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }
        super.onDraw(canvas);
        this.invalidate();
    }

}