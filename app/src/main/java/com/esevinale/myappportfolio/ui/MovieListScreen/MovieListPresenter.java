package com.esevinale.myappportfolio.ui.MovieListScreen;


import com.esevinale.myappportfolio.models.MovieItem;

public interface MovieListPresenter {

    enum ProgressType {
        Refreshing, ListProgress, Paging
    }

    void loadStart();
    void loadNext(int page);
    void loadRefresh();
    void onOptionItemSelected(byte loadType);
    void onMovieClicked(MovieItem movieItem);
}
