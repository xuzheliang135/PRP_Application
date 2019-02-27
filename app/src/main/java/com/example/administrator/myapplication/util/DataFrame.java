package com.example.administrator.myapplication.util;

public class DataFrame {
    private int[] ch;
    private int timestamp;

    DataFrame(int[] ch, int timestamp) {
        this.ch = ch;
        this.timestamp = timestamp;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public float getChannelData(int index) {
        return ch[index];
    }
}

