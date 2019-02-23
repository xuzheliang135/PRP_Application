package com.example.administrator.myapplication.AbstractClass;

import java.util.LinkedList;

public abstract class BaseSQLUil {
    abstract LinkedList<BaseRecord> get_date_list();

    abstract BaseRecord get_record(long date);

    abstract void record(BaseRecord record);

    abstract void init();//检查是否存在对应表，如果没有则创建
}
