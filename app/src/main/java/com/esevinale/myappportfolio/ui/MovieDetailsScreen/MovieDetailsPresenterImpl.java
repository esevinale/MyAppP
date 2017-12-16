package com.esevinale.myappportfolio.ui.MovieDetailsScreen;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.esevinale.myappportfolio.api.ApiConstants;
import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.application.AppController;
import com.esevinale.myappportfolio.models.FullVideo;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.models.Video;
import com.esevinale.myappportfolio.utils.Constants;
import com.esevinale.myappportfolio.utils.manager.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MovieDetailsPresenterImpl extends MvpPresenter<MovieDetailsView> implements MovieDetailsPresenter {


    @Inject
    TmdbService tmdbService;

    @Inject
    NetworkManager networkManager;

    MovieDetailsPresenterImpl() {
        AppController.getAppComponent().inject(this);
    }


    @Override
    public void movieItemReady(MovieItem movieItem) {
        getViewState().showMovieDetails(movieItem);
        getViewState().showMoviePoster(ApiConstants.BACK_TMDB_URL + movieItem.getBackdropPath());

        loadTrailers(movieItem.getId());
    }

    @Override
    public void onTrailerClicked(String url) {
        getViewState().startTrailerIntent(url);
    }

    private void loadTrailers(int key) {
        tmdbService.getYoutubeFrailers(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::trailersLoaded,
                error -> {});
    }

    private void trailersLoaded(FullVideo video) {
        List<Video> youtubeTrailers = new ArrayList<>();
        for (Video trailer : video.getResults())
            if (trailer.getSite().equals(Constants.YOUTUBE))
                youtubeTrailers.add(trailer);
        if (youtubeTrailers.size() > 0)
            getViewState().showTrailers(youtubeTrailers);
    }
}
