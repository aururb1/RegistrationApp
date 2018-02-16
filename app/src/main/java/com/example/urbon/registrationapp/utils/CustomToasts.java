package com.example.urbon.registrationapp.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by urbon on 2/16/2018.
 */

public class CustomToasts {

    Activity activity;

    public CustomToasts(Activity activity) {
        this.activity = activity;
    }

    public void shortToast(String text){
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String text){
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
    }
}
