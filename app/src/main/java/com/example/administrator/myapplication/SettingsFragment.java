package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_settings, container, false);
        view.findViewById(R.id.setting_about).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setting_about) {
            Intent start_intent = new Intent();
            start_intent.setClass(Objects.requireNonNull(getActivity()).getApplicationContext(), AboutActivity.class);
            startActivity(start_intent);
        }
    }
}
