package com.example.lucakoval.apposition.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.lucakoval.apposition.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import backend.Day;
import backend.Period;
import backend.UserData;

public class LocalDataHandler {
    Context context;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    public LocalDataHandler(Context context_) {
        context = context_;

        settings = context.getSharedPreferences(context.getString(R.string.preferencesFileKey), Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public UserData getUserData() {
        return new Gson().fromJson(settings.getString(context.getString(R.string.userDataKey), null), UserData.class);
    }

    public void setUserData(UserData data) {
        editor.putString(context.getString(R.string.userDataKey), new Gson().toJson(data));
        editor.commit();
    }

    public ArrayList<Day> getSchedule() {
        ArrayList<Day> schedule = new Gson().fromJson(settings.getString(context.getString(R.string.scheduleKey), null), new TypeToken<ArrayList<Day>>(){}.getType());
        if(schedule == null || schedule.size() < Day.numberOfDays) {
            System.out.println("Setup");
            schedule = new ArrayList<Day>(Day.numberOfDays);
            for(int a = 0; a < Day.numberOfDays; a++) schedule.add(null);
            setSchedule(schedule);
        }

        return schedule;
    }

    public Day getDay(int dayIndex) {
        ArrayList<Day> schedule = getSchedule();
        try {
            return schedule.get(dayIndex);
        } catch (Exception e) {
            return null;
        }
    }

    public void setSchedule(ArrayList<Day> days) {
        editor.putString(context.getString(R.string.scheduleKey), new Gson().toJson(days));
        editor.commit();
    }

    public void setDay(int index, Day day) {
        ArrayList<Day> schedule = getSchedule();
        schedule.set(index, day);
        setSchedule(schedule);
    }

    public void setPeriod(int dayIndex, int periodIndex, Period period) {
        Day day = getSchedule().get(dayIndex);
        day.setPeriod(periodIndex, period);
        setDay(dayIndex, day);
    }

    public void clearAllData() {
        editor.clear();
        editor.commit();
    }
}