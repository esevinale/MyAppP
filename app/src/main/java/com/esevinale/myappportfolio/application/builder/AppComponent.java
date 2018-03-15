package com.esevinale.myappportfolio.application.builder;

import com.esevinale.myappportfolio.ui.BaseActivity;
import com.esevinale.myappportfolio.ui.MovieListScreen.MovieListActivity;
import com.esevinale.myappportfolio.ui.MovieDetailsScreen.MovieDetailsActivity;
import com.esevinale.myappportfolio.utils.manager.MyPreferencesManager;
import com.esevinale.myappportfolio.utils.manager.NetworkManager;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {AppModule.class, ManagerModule.class, RestModule.class})
public interface AppComponent {

    void inject(BaseActivity activity);
    void inject(MovieListActivity activity);
    void inject(MovieDetailsActivity activity);

    void inject(NetworkManager manager);
    void inject(MyPreferencesManager manager);

    MovieListComponent createMovieListComponent(MovieListModule movieListModule);
    MovieDetailsComponent createMovieDetailsComponent(MovieDetailsModule movieDetailsModule);
}
