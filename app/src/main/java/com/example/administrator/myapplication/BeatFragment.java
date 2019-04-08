package com.example.administrator.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.administrator.myapplication.util.BluetoothReceiver;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class BeatFragment extends Fragment implements View.OnClickListener {
    public Button controlButton;
    private Button chButton;
    private EditText editCh;
    private BluetoothReceiver bluetoothReceiver;
    private IntentFilter bluetoothFilter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_show_beats, container, false);
        controlButton = view.findViewById(R.id.controlButton);
        chButton = view.findViewById(R.id.chButton);
        editCh = view.findViewById(R.id.editCh);
        bluetoothReceiver = new BluetoothReceiver((BeatView) view.findViewById(R.id.draw_content), this);
        controlButton.setOnClickListener(this);
        chButton.setOnClickListener(this);
        createFilter();
        return view;
    }


    private void createFilter() {
        bluetoothFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        bluetoothFilter.addAction(BluetoothDevice.ACTION_FOUND);
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
    }

    private void requestBluetooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled()) {
//            adapter.enable();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
            }
        }
    }

    private void startBluetoothDiscovery() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.startDiscovery();
    }

    @Override
    public void onClick(View v) {
        //更改显示通道数
        if (v.getId() == R.id.chButton) {
            if (editCh.length() > 0) {
                Config.channelNumber = Integer.parseInt(editCh.getText().toString());
                Toast.makeText(this.getContext(), "显示通道数已变为" + Config.channelNumber, Toast.LENGTH_LONG).show();
            }
        }
        if (v.getId() == R.id.controlButton) {
            if (controlButton.getText() == getResources().getString(R.string.start)) {
                requestBluetooth();
                ((BeatView) getView().findViewById(R.id.draw_content)).getModel().clearAll();
                getActivity().registerReceiver(bluetoothReceiver, bluetoothFilter);
                startBluetoothDiscovery();
                controlButton.setText(R.string.terminate);
            } else if (controlButton.getText() == getResources().getString(R.string.terminate)) {
                bluetoothReceiver.terminateRecord();
                controlButton.setText(R.string.start);
                try {
                    getActivity().unregisterReceiver(bluetoothReceiver);
                } catch (Exception ignored) {
                }
            }
        }
    }
}

