package com.esevinale.myappportfolio.ui.MovieScreen;


public interface MovieListPresenter {

    enum ProgressType {
        Refreshing, ListProgress, Paging
    }

    void loadStart();
    void loadNext(int page);
    void loadRefresh();
    void onOptionItemSelected(byte loadType);
}
