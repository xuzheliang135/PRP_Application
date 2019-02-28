package com.example.administrator.myapplication.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.example.administrator.myapplication.BeatView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothReceiver extends BroadcastReceiver {
    private final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket btSocket;
    private DataManager dataManager = new DataManager();//todo:reconstruct DataManager as a factory
    private BeatImage beatImage;
    private boolean isRunning = false;

    private byte[] start_command = new byte[]{Integer.valueOf(0xA0).byteValue(), 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05, Integer.valueOf(0xA6).byteValue()};
    private byte[] end_command = new byte[]{Integer.valueOf(0xA0).byteValue(), 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05, Integer.valueOf(0xA7).byteValue()};

    public BluetoothReceiver(BeatView beatView) {
        this.beatImage = beatView.getModel();
    }

    private synchronized boolean isRunning() {
        return isRunning;
    }

    private void recordAndShow() {
        try {
            OutputStream out = btSocket.getOutputStream();
            out.write(start_command);
            InputStream in = btSocket.getInputStream();
            byte[] buf = new byte[64];
            DataFrame frame;
            while (isRunning()) {
                int len = in.read(buf);
                dataManager.append(buf, len);
                if (dataManager.isReady()) {
                    frame = dataManager.getFrame();
                    beatImage.append((int) (frame.getChannelData(2)));
                }
            }
            dataManager.clearAll();
            out.write(end_command);
            out.close();
            in.close();
            //todo save record
        } catch (IOException e) {
            Log.d("my_debug", "error:" + e.getMessage());
            startRecord();
        }
    }

    public boolean isConnected() {
        if (btSocket == null) return false;
        return btSocket.isConnected();
    }

    private synchronized void startRecord() {
        isRunning = true;
//        beatImage.clearAll();
        new Thread() {
            @Override
            public void run() {
                recordAndShow();
            }
        }.start();

    }

    public synchronized void terminateRecord() {
        isRunning = false;
    }

    private void connect(final BluetoothDevice device) {
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            btSocket.connect();
            startRecord();
            Log.d("my_debug", "." + btSocket.isConnected());
        } catch (IOException e) {
            Log.d("my_debug", "error:" + e.getMessage());
        }
    }

    public void connect() {
        try {
            btSocket = btSocket.getRemoteDevice().createRfcommSocketToServiceRecord(MY_UUID);
            btSocket.connect();
            startRecord();
            Log.d("my_debug", "." + btSocket.isConnected());
        } catch (IOException e) {
            Log.d("my_debug", "error:" + e.getMessage());
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            Toast toast = Toast.makeText(context, "开始搜索 ...", Toast.LENGTH_LONG);
            Log.d("my_debug", "开始搜索 ...");
            toast.show();
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            Log.d("my_debug", "搜索结束");
        }
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            Log.d("my_debug", "发现 ...");
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String a = device.getName();
            if (a == null) a = "asd";
            Log.d("my_debug", a);
            if (a.contains("you")) {
                Log.d("my_debug", "发现目标蓝牙");
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                connect(device);
            }
        }
    }
}

