package com.esevinale.myappportfolio.ui.MovieDetailsScreen;

import com.arellomobile.mvp.MvpView;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.models.Video;

import java.util.List;


public interface MovieDetailsView extends MvpView {
    void showMovieDetails(MovieItem movieItem);
    void showMoviePoster(String path);
    void showTrailers(List<Video> video);
    void startTrailerIntent(String url);
}
