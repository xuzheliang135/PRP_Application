package com.example.administrator.myapplication.AbstractClass;

import android.graphics.Path;

import java.util.LinkedList;

public abstract class BaseBeatImage {
    LinkedList<Path> path;

    abstract void set_start(int start);

    abstract LinkedList<Path> get_paths();

    abstract int get_length();

    abstract void set_width(int width);

    abstract void set_height(int height);

    abstract void set_record(BaseRecord record);
}
