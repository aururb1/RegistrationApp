package com.example.urbon.registrationapp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by urbon on 2/16/2018.
 */

public class CustomToasts {

    Context context;

    public CustomToasts(Context context) {
        this.context = context;
    }

    public void shortToast(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
