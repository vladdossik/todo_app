package com.example.todo_app;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref{
    SharedPreferences sharedPreferences;
    public SharedPref(Context context){
sharedPreferences=context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }
    public void setNigthModeState(Boolean state){
      SharedPreferences.Editor editor =sharedPreferences.edit();
      editor.putBoolean("NightMode",state);
      editor.commit();
    }
    public boolean loadNightState(){
        Boolean state=sharedPreferences.getBoolean("NightMode",false);
        return state;
    }
}
