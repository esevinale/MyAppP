package com.esevinale.myappportfolio.ui.MovieDetailsScreen;

import com.esevinale.myappportfolio.api.ApiConstants;
import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.models.FullVideo;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.models.Video;
import com.esevinale.myappportfolio.utils.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MovieDetailsPresenterTest {

    @Mock
    TmdbService service;
    private MovieDetailsPresenterImpl presenter;
    @Mock
    MovieDetailsView view;
    @Mock
    MovieDetailsView$$State viewState;
    @Captor
    private ArgumentCaptor<List<Video>> trailers;

    @Before
    public void setUp() throws Exception {
        presenter = new MovieDetailsPresenterImpl(service);
        presenter.attachView(view);
        presenter.setViewState(viewState);
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Test
    public void testCreated() throws Exception {
        assertNotNull(presenter);
    }

    @Test
    public void movieItemReady() throws Exception {
        Mockito.when(service.getYoutubeTrailers(1)).thenReturn(Observable.empty());
        MovieItem mv = createMovieItem();
        presenter.movieItemReady(mv);
        Mockito.verify(viewState).showMovieDetails(mv);
        Mockito.verify(viewState).showMoviePoster(ApiConstants.BACK_TMDB_URL);

        Mockito.when(service.getYoutubeTrailers(1)).thenReturn(Observable.error(new IOException()));
        presenter.movieItemReady(mv);
        Mockito.verify(viewState, Mockito.times(2)).showMovieDetails(mv);
        Mockito.verify(viewState, Mockito.times(2)).showMoviePoster(ApiConstants.BACK_TMDB_URL);
    }


    @Test
    public void movieItemReadyWithTrailers() throws Exception {
        Mockito.when(service.getYoutubeTrailers(1)).thenReturn(Observable.just(createFakeFullVideo()));
        MovieItem movieItem = createMovieItem();
        presenter.movieItemReady(movieItem);
        Mockito.verify(viewState).showMovieDetails(movieItem);
        Mockito.verify(viewState).showMoviePoster(ApiConstants.BACK_TMDB_URL);
        Mockito.verify(viewState).showTrailers(trailers.capture());
    }

    @Test
    public void onTrailerClicked() throws Exception {
        presenter.onTrailerClicked("test");

        Mockito.verify(viewState).startTrailerIntent("test");
    }

    private MovieItem createMovieItem() {
        MovieItem movieItem = new MovieItem();
        movieItem.setId(1);
        movieItem.setBackdropPath("");
        return movieItem;
    }

    private FullVideo createFakeFullVideo() {
        List<Video> videoList = new ArrayList<>();
        Video trailer = new Video();
        trailer.setSite(Constants.YOUTUBE);
        videoList.add(trailer);
        FullVideo fv = new FullVideo();
        fv.setResults(videoList);
        return fv;
    }
}