package io.infernodz.equalizerapp;


import android.app.Application;

import io.infernodz.equalizerapp.di.AppComponent;
import io.infernodz.equalizerapp.di.DaggerAppComponent;
import io.infernodz.equalizerapp.di.EqualizerModule;

public class EqualizerApplication extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .equalizerModule(new EqualizerModule())
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
