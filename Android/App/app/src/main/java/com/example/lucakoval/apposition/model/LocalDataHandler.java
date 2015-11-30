package com.example.lucakoval.apposition.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.lucakoval.apposition.R;
import com.google.gson.Gson;

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
        editor.apply();
    }

    public void clearAllData() {
        editor.clear();
        editor.apply();
    }
}