package com.esevinale.myappportfolio.application.builder;

import com.esevinale.myappportfolio.ui.BaseActivity;
import com.esevinale.myappportfolio.ui.MovieScreen.MainActivity;
import com.esevinale.myappportfolio.ui.MovieScreen.MovieListFragment;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {AppModule.class, ManagerModule.class, RestModule.class})
public interface AppComponent {

    void inject(BaseActivity activity);
    void inject(MainActivity activity);

    void inject(MovieListFragment fragment);
}
