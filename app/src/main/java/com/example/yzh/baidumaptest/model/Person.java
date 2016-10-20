package com.example.yzh.baidumaptest.model;

/**
 * Created by YZH on 2016/10/15.
 */

public class Person {
    public static final String TYPE_FRIEND = "friend";
    public static final String TYPE_ENERMY = "enermy";

    private String name;
    private String number;
    private String latitude;
    private String longitude;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
