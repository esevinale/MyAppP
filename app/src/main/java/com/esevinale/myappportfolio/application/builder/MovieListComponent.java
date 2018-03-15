package com.esevinale.myappportfolio.application.builder;

import com.esevinale.myappportfolio.ui.MovieListScreen.MovieListPresenterImpl;
import com.esevinale.myappportfolio.ui.MovieListScreen.MovieListScope;

import dagger.Subcomponent;

@MovieListScope
@Subcomponent(modules = { MovieListModule.class})
public interface MovieListComponent {
    MovieListPresenterImpl providePresenter();
}
