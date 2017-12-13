package com.esevinale.myappportfolio.application.builder;

import com.esevinale.myappportfolio.utils.manager.MyFragmentManager;
import com.esevinale.myappportfolio.utils.manager.NetworkManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ManagerModule {

    @Singleton
    @Provides
    MyFragmentManager provideMyFragmentManager() {
        return new MyFragmentManager();
    }

    @Singleton
    @Provides
    NetworkManager provideNetworkManager() {
        return new NetworkManager();
    }
}
