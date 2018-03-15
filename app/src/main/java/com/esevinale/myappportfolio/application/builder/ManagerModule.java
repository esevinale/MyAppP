package com.esevinale.myappportfolio.application.builder;

import android.content.Context;

import com.esevinale.myappportfolio.utils.manager.MyFragmentManager;
import com.esevinale.myappportfolio.utils.manager.MyPreferencesManager;
import com.esevinale.myappportfolio.utils.manager.NetworkManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ManagerModule {

    @Singleton
    @Provides
    NetworkManager provideNetworkManager() {
        return new NetworkManager();
    }

    @Singleton
    @Provides
    MyPreferencesManager providePreferencesManager(Context context) {
        return new MyPreferencesManager(context);
    }
}
