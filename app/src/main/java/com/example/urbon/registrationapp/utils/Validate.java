package com.example.urbon.registrationapp.utils;

/**
 * Created by urbon on 3/7/2018.
 */

public class Validate {

    public boolean validateStringInput(String value){
        if (value.isEmpty()) {
            return false;
        }
        return true;
    }
}
