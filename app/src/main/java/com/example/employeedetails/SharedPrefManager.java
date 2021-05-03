package com.example.employeedetails;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private Context context;
    SharedPreferences SharedPref;
    SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        this.context=context;
        SharedPref = context.getSharedPreferences("AIDataLabSharedPref", Context.MODE_PRIVATE);
        editor= SharedPref.edit();
    }

    public boolean saveLongValue(String key,long value){
        editor.putLong(key,value).apply();
        return true;
    }
    public long getLongValue(String key,long defaultValue){
        return SharedPref.getLong("key",defaultValue);
    }

    public boolean saveStringValue(String key, String value){
        editor.putString(key,value).apply();
        return true;
    }

    public boolean saveBoolValue(String key,Boolean value){
        editor.putBoolean(key,value).apply();
        return true;
    }
    public boolean saveIntValue(String key,int value){
        editor.putInt(key,value).apply();
        return true;
    }

    public String getStringValue(String key,String defaultValue){
        return SharedPref.getString(key,defaultValue);
    }

    public boolean getBoolValue(String key,boolean defaultValue){
        return SharedPref.getBoolean(key,defaultValue);
    }

    public int getIntValue(String key,int defaultValue){
        return SharedPref.getInt(key,defaultValue);
    }

    public boolean isUserSignedIn(String key){
        return SharedPref.getBoolean(key,false);
    }
}



