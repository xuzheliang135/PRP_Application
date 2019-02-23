package com.example.administrator.myapplication;

import java.util.Date;

public class RecordItem {
    Date date;
    int beats_per_minute;
    RecordItem(long date,int beats_per_minute){
        this.beats_per_minute=beats_per_minute;
        this.date=new Date(date);
    }
    String get_date(){
        return date.toString();
    }
    String get_beats(){
        return String.valueOf(beats_per_minute);
    }
}
