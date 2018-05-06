package com.example.urbon.registrationapp.dagger;

import android.content.Context;

import com.example.urbon.registrationapp.utils.CustomToasts;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyModule {
    Context context;

    public MyModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public CustomToasts provideToasts() {
        return new CustomToasts(context);
    }
}
