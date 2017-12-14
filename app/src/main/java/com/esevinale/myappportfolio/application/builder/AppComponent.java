package com.esevinale.myappportfolio.application.builder;

import com.esevinale.myappportfolio.ui.BaseActivity;
import com.esevinale.myappportfolio.ui.MainActivity;
import com.esevinale.myappportfolio.ui.MovieDetailsScreen.MovieDetailsActivity;
import com.esevinale.myappportfolio.ui.MovieScreen.MovieListFragment;
import com.esevinale.myappportfolio.ui.MovieScreen.MovieListPresenter;
import com.esevinale.myappportfolio.utils.manager.NetworkManager;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {AppModule.class, ManagerModule.class, RestModule.class})
public interface AppComponent {

    void inject(BaseActivity activity);
    void inject(MainActivity activity);
    void inject(MovieDetailsActivity activity);

    void inject(MovieListFragment fragment);

    void inject(MovieListPresenter presenter);

    void inject(NetworkManager manager);
}
