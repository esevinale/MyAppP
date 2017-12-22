package com.esevinale.myappportfolio.application.builder;

import com.esevinale.myappportfolio.ui.MovieScreen.MovieListPresenterImpl;
import com.esevinale.myappportfolio.ui.MovieScreen.MovieListScope;

import dagger.Subcomponent;

@MovieListScope
@Subcomponent(modules = { MovieListModule.class})
public interface MovieListComponent {
    void inject(MovieListPresenterImpl presenter);
}
