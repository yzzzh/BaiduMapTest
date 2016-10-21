package com.example.yzh.baidumaptest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yzh.baidumaptest.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZH on 2016/10/16.
 */

public class RadarDB {
    //数据库名字
    public static final String DB_NAME = "Radar.db";
    //数据库版本
    public static final int VERSION = 1;
    //保证只有一个coolWeatherDB
    private static RadarDB radarDB;
    //以coolWeatherDB的名义，实际上操作的还是db
    private SQLiteDatabase db;

    private RadarDB(Context context){
        RadarOpenHelper radarOpenHelper = new RadarOpenHelper(context,DB_NAME,null,VERSION);
        db = radarOpenHelper.getWritableDatabase();
    }

    public synchronized static RadarDB getInstance(Context context){
        if (radarDB == null)
            radarDB = new RadarDB(context);
        return radarDB;
    }
    //保存至数据库
    public void addPerson(Person person){
        Cursor cursor = db.query("person",null,"number = ?",new String[]{person.getNumber()},null,null,null);
        if (!cursor.moveToFirst()) {
            if (person != null) {
                    ContentValues values = new ContentValues();
                    values.put("name", person.getName());
                    values.put("number", person.getNumber());
                    values.put("latitude", person.getLatitude());
                    values.put("longitude", person.getLongitude());
                    values.put("type", person.getType());
                    db.insert("person", null, values);
            }
        }else {
            radarDB.updatePerson(person);
        }
    }
    //取出一群朋友或敌人
    public List<Person> loadPeople(String type){
        List<Person> persons = new ArrayList<Person>();
        if (type.equals("friend") || type.equals("enermy")){
            Cursor cursor = db.query("person",null,"type = ?",new String[]{type},null,null,null);
            if (cursor.moveToFirst()){
                do {
                    Person person = new Person();
                    person.setName(cursor.getString(cursor.getColumnIndex("name")));
                    person.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                    person.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                    person.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                    person.setType(cursor.getString(cursor.getColumnIndex("type")));

                    persons.add(person);
                }while (cursor.moveToNext());

                return persons;
            }else
                return persons;
        }else
            return persons;
    }
    //取出一个人
    public Person getPerson(String number){
        Cursor cursor = db.query("person",null,"number = ?",new String[]{number},null,null,null);
        Person person = new Person();
        if (cursor.moveToFirst()){
            person.setName(cursor.getString(cursor.getColumnIndex("name")));
            person.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            person.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
            person.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
            person.setType(cursor.getString(cursor.getColumnIndex("type")));
            return person;
        }else {
            return person;
        }
    }
    //删除某个人
    public void delPerson(String number){
        db.delete("person","number = ?",new String[]{number});
    }

    //更新某个人资料
    public void updatePerson(Person person){
        if (person != null){
            ContentValues values = new ContentValues();
            values.put("number",person.getNumber());
            values.put("latitude",person.getLatitude());
            values.put("longitude",person.getLongitude());
            db.update("person",values,"number = ?",new String[]{person.getNumber()});
        }
    }
}
