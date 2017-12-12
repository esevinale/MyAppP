package com.esevinale.myappportfolio.application;

import android.app.Application;

import com.esevinale.myappportfolio.application.builder.AppComponent;
import com.esevinale.myappportfolio.application.builder.AppModule;
import com.esevinale.myappportfolio.application.builder.DaggerAppComponent;


public class AppController extends Application{

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initComponent();
    }

    private void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this)).build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
