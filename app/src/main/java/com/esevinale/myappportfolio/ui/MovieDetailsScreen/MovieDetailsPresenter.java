package com.esevinale.myappportfolio.ui.MovieDetailsScreen;

import com.esevinale.myappportfolio.models.MovieItem;

public interface MovieDetailsPresenter {
    void movieItemReady(MovieItem movieItem);
    void onTrailerClicked(String url);
}
