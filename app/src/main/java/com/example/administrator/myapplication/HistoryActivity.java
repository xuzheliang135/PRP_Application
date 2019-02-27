package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.example.administrator.myapplication.util.Record;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    long date;
    int beats_per_minute;
    Record record;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        findViewById(R.id.ret).setOnClickListener(this);
        Intent intent = getIntent();
        record = (Record) intent.getSerializableExtra("record");
        TextView date = findViewById(R.id.date);
        TextView beats_per_minute = findViewById(R.id.beats_per_minute);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ret) {
            finish();
        }
    }
}