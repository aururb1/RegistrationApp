package com.example.urbon.registrationapp;

import android.app.Application;

import com.example.urbon.registrationapp.dagger.DaggerMyComponent;
import com.example.urbon.registrationapp.dagger.MyComponent;
import com.example.urbon.registrationapp.dagger.MyModule;

public class MyApplication extends Application {
    private MyComponent myComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        myComponent = createMyComponent();
    }

    public MyComponent getMyComponent() {
        return myComponent;
    }

    private MyComponent createMyComponent() {
        return DaggerMyComponent
                .builder()
                .myModule(new MyModule(getApplicationContext()))
                .build();
    }
}
