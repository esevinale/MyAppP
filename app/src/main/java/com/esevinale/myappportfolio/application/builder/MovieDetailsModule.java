package com.esevinale.myappportfolio.application.builder;

import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.ui.MovieDetailsScreen.MovieDetailsPresenterImpl;
import com.esevinale.myappportfolio.ui.MovieDetailsScreen.MovieDetailsScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieDetailsModule {

    @MovieDetailsScope
    @Provides
    MovieDetailsPresenterImpl providePresenter(TmdbService tmdbService) {
        return new MovieDetailsPresenterImpl(tmdbService);
    }
}
