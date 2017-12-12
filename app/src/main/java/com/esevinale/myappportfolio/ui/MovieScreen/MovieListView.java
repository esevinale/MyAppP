package com.esevinale.myappportfolio.ui.MovieScreen;

import com.esevinale.myappportfolio.models.MovieItem;

import java.util.List;


public interface MovieListView {
    void showMovies(List<MovieItem> movies);
    void loadingStarted();
    void loadingFailed(String errorMessage);
    void onMovieClicked(MovieItem movie);
}
