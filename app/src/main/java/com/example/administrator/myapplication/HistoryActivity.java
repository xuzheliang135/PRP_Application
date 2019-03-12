package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.myapplication.util.Record;
import com.example.administrator.myapplication.util.SQLUtil;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    long date;
    Record record;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        findViewById(R.id.ret).setOnClickListener(this);
        Intent intent = getIntent();
        date = intent.getLongExtra("date", -1);
        if (date == -1) Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
        record = new SQLUtil(getApplicationContext()).get_record(date);

        TextView date = findViewById(R.id.date);
        date.setText(record.getDate_str());
        ((BeatView) findViewById(R.id.imageView)).getModel().setRecord(record);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ret) {
            finish();
        }
    }
}