package com.esevinale.myappportfolio.ui.MovieDetailsScreen;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.esevinale.myappportfolio.api.ApiConstants;
import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.models.FullVideo;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.models.Video;
import com.esevinale.myappportfolio.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MovieDetailsPresenterImpl extends MvpPresenter<MovieDetailsView> implements MovieDetailsPresenter {


    private TmdbService tmdbService;

    public MovieDetailsPresenterImpl(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
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
        tmdbService.getYoutubeTrailers(key)
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
