package com.esevinale.myappportfolio.application.builder;

import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.ui.MovieScreen.MovieContentInteractorImpl;
import com.esevinale.myappportfolio.ui.MovieScreen.MovieListScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieListModule {

    @MovieListScope
    @Provides
    MovieContentInteractorImpl provideMovieContentInterator(TmdbService tmdbService) {
        return new MovieContentInteractorImpl(tmdbService);
    }
}
