package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Date;

public class HistoryActivity extends AppCompatActivity {
    long date;
    int beats_per_minute;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Intent intent = getIntent();
        date = intent.getLongExtra("date", 0);
        TextView date=findViewById(R.id.date);
        TextView beats_per_minute=findViewById(R.id.beats_per_minute);
        date.setText(new Date(this.date).toString());
        beats_per_minute.setText("0");
    }
}
