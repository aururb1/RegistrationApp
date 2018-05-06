package com.example.urbon.registrationapp.dagger;

import com.example.urbon.registrationapp.activities.InformationActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MyModule.class)
public interface MyComponent {
    void inject(InformationActivity informationActivity);
}
