package com.esevinale.myappportfolio.application.builder;

import com.esevinale.myappportfolio.ui.MovieDetailsScreen.MovieDetailsPresenterImpl;
import com.esevinale.myappportfolio.ui.MovieDetailsScreen.MovieDetailsScope;

import dagger.Subcomponent;

@MovieDetailsScope
@Subcomponent(modules = { MovieDetailsModule.class})
public interface MovieDetailsComponent {
    MovieDetailsPresenterImpl providePresenter();
}
