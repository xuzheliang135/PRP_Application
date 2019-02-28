package com.example.administrator.myapplication.util;

import java.util.LinkedList;

public class DataManager {
    private volatile LinkedList<Byte> data;
    private volatile LinkedList<DataFrame> frames;
    private int frameLength = 16;
    private byte end_1 = Integer.valueOf(0x0D).byteValue();
    private byte end_2 = Integer.valueOf(0x0A).byteValue();

    DataManager() {
        data = new LinkedList<Byte>();
        frames = new LinkedList<DataFrame>();
        new Thread() {
            @Override
            public void run() {
                int count = 0;
                while (true) {
                    DataFrame frame = getOneFrame();
                    if (count++ > 10 && frame != null) {
                        frames.add(frame);
                        count = 0;
                    }
                }
            }
        }.start();

    }

    synchronized void append(byte[] new_data, int len) {
        for (int i = 0; i < len; i++) data.add(new_data[i]);
    }

    public void append(int new_date) {
        data.add(Integer.valueOf(new_date).byteValue());
    }

    synchronized boolean isReady() {
        return frames.size() > 1;//todo: fix when size is 1,removeFirst() throw exception
    }

    private synchronized DataFrame getOneFrame() {
        boolean ready = false;
        while (!ready) {
            if (data.size() < frameLength + 2) return null;
            int index = data.indexOf(end_1);
            for (int i = 0; i < index; i++) data.removeFirst();
            if (data.get(1) != end_2
                    || data.get(frameLength) != end_1
                    || data.get(frameLength + 1) != end_2) {
                data.removeFirst();
                continue;
            }
            ready = true;
        }
        byte[] bytes = new byte[frameLength];
        for (int i = 0; i < frameLength; i++) bytes[i] = data.removeFirst();
        return parse(bytes);
    }

    private DataFrame parse(byte[] bytes) {
        byte[] tmp = bytes.clone();
        if (tmp.length - 2 >= 0) System.arraycopy(tmp, 2, bytes, 0, tmp.length - 2);
        int[] frameData = new int[8];
        for (int i = 0; i < 4; i++) {
            frameData[i * 2] = bytes[i * 2 + 1] << 4 | bytes[8 + i] >> 4;
            frameData[i * 2 + 1] = bytes[i * 2 + 1] << 4 | (bytes[8 + i] & 0x0f);
        }
        int timestamp = bytes[12] << 8 | bytes[13];
        return new DataFrame(frameData, timestamp);
    }

    synchronized DataFrame getFrame() {
        if (!isReady()) return null;
        else return frames.removeFirst();
    }

    synchronized LinkedList<DataFrame> getFrames() {
        return frames;
    }

    public synchronized LinkedList<Byte> getData() {
        return data;
    }

    public void clearAll() {
        data.clear();
        frames.clear();
    }
}
