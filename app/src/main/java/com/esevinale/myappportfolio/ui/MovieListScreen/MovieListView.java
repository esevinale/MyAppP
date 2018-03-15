package com.esevinale.myappportfolio.ui.MovieListScreen;

import com.arellomobile.mvp.MvpView;
import com.esevinale.myappportfolio.models.MovieItem;

import java.util.List;


public interface MovieListView extends MvpView{

    void showRefreshing();
    void hideRefreshing();

    void showListProgress();
    void hideListProgress();

    void showError();

    void setMovies(List<MovieItem> movies);
    void addMovies(List<MovieItem> movies);

    void onMovieClicked(MovieItem movie);
    void startMovieDetails(MovieItem movie);
}
