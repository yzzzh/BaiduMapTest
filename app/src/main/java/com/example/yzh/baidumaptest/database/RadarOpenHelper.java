package com.example.yzh.baidumaptest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YZH on 2016/10/15.
 */

public class RadarOpenHelper extends SQLiteOpenHelper {
    private static final String CREATE_PERSON = "create table person(" +
            "id integer primary key autoincrement," +
            "name text," +
            "number text," +
            "latitude text," +
            "longitude text," +
            "type text)";

    public RadarOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PERSON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

