package com.esevinale.myappportfolio.application.builder;

import com.esevinale.myappportfolio.MyIdlingResources;
import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.repository.MovieContentInteractor;
import com.esevinale.myappportfolio.repository.MovieContentInteractorImpl;
import com.esevinale.myappportfolio.ui.MovieListScreen.MovieListPresenterImpl;
import com.esevinale.myappportfolio.ui.MovieListScreen.MovieListScope;
import com.esevinale.myappportfolio.utils.manager.MyPreferencesManager;
import com.esevinale.myappportfolio.utils.manager.NetworkManager;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieListModule {

    @MovieListScope
    @Provides
    MovieContentInteractor provideMovieContentInterator(TmdbService tmdbService) {
        return new MovieContentInteractorImpl(tmdbService);
    }

    @MovieListScope
    @Provides
    MovieListPresenterImpl provideMovieListPresenter(MovieContentInteractor movieContentInteractor, NetworkManager networkManager, MyPreferencesManager preferencesManager) {
        return new MovieListPresenterImpl(movieContentInteractor, networkManager, preferencesManager, new MyIdlingResources());
    }

}
