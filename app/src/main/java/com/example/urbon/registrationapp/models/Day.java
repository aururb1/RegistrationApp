package com.example.urbon.registrationapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by urbon on 4/5/2018.
 */

public class Day {
    private String date;
    private List<Hour> hours = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Hour> getHours() {
        return hours;
    }

    public void setHours(List<Hour> hours) {
        this.hours = hours;
    }

    public void addHour(Hour hour){
        hours.add(hour);
    }

    public void removeHour(Hour hour){
        hours.remove(hour);
    }
}
