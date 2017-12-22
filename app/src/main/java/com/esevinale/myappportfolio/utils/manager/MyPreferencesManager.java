package com.esevinale.myappportfolio.utils.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferencesManager {

    private static final String PREF_FILER = "filer";
    private static final String PREF_NAME = "FilterPref";

    private SharedPreferences sharedPreferences;

    public MyPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setSelectedOption(byte loadType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_FILER, loadType);
        editor.apply();
    }

    public int getSelectedOption() {
        return sharedPreferences.getInt(PREF_FILER, 0);
    }
}
