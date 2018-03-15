package com.esevinale.myappportfolio.ui.MovieListScreen;

import com.esevinale.myappportfolio.MyIdlingResources;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.repository.MovieContentInteractor;
import com.esevinale.myappportfolio.utils.Constants;
import com.esevinale.myappportfolio.utils.manager.MyPreferencesManager;
import com.esevinale.myappportfolio.utils.manager.NetworkManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static junit.framework.Assert.assertNotNull;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class MovieListPresenterTest {

    private MovieListPresenterImpl presenter;

    @Mock
    NetworkManager networkManager;

    @Mock
    MovieContentInteractor interactor;

    @Mock
    MyPreferencesManager preferencesManager;

    @Mock
    MyIdlingResources myIdlingResources;

    @Mock
    MovieListView view;

    @Mock
    MovieListView$$State viewState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new MovieListPresenterImpl(interactor, networkManager, preferencesManager, myIdlingResources);
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
    public void movieLoadStartWithInternet() throws Exception {
        Mockito.when(networkManager.getNetworkObservable(Constants.TMDB)).thenReturn(Observable.just(true));
        Mockito.when(interactor.loadData(1, Constants.POPULAR)).thenReturn(Observable.fromArray(emptyMovies()));

        presenter.loadStart();

        Mockito.verify(viewState).showListProgress();
        Mockito.verify(viewState).hideListProgress();
        Mockito.verify(viewState).setMovies(emptyMovies());
    }

    @Test
    public void movieLoadStartWithInternetError() throws Exception {
        Mockito.when(networkManager.getNetworkObservable(Constants.TMDB)).thenReturn(Observable.just(true));
        Mockito.when(interactor.loadData(1, Constants.POPULAR)).thenReturn(Observable.error(new IOException()));

        presenter.loadStart();

        Mockito.verify(viewState).showListProgress();
        Mockito.verify(viewState).hideListProgress();
        Mockito.verify(viewState).showError();
    }

    @Test
    public void movieLoadStartNoInternet() throws Exception {
        Mockito.when(networkManager.getNetworkObservable(Constants.TMDB)).thenReturn(Observable.just(false));
        Mockito.when(interactor.restoreData(Constants.POPULAR)).thenReturn(Observable.fromArray(emptyMovies()));

        presenter.loadStart();

        Mockito.verify(viewState).showListProgress();
        Mockito.verify(viewState).hideListProgress();
        Mockito.verify(viewState).setMovies(emptyMovies());
    }

    @Test
    public void onOptionItemSelected() throws Exception {
        Mockito.when(networkManager.getNetworkObservable(Constants.TMDB)).thenReturn(Observable.just(true));
        Mockito.when(interactor.loadData(1, Constants.POPULAR)).thenReturn(Observable.fromArray(emptyMovies()));
        Mockito.when(interactor.loadData(1, Constants.LATEST)).thenReturn(Observable.fromArray(emptyMovies()));

        presenter.onOptionItemSelected(Constants.POPULAR);

        Mockito.verify(viewState).showListProgress();
        Mockito.verify(viewState).hideListProgress();
        Mockito.verify(viewState).setMovies(emptyMovies());

        presenter.onOptionItemSelected(Constants.LATEST);

        Mockito.verify(viewState, Mockito.times(2)).showListProgress();
        Mockito.verify(viewState, Mockito.times(2)).hideListProgress();
        Mockito.verify(viewState, Mockito.times(2)).setMovies(emptyMovies());
    }

    @Test
    public void movieOnLoadNext() throws Exception {
        Mockito.when(networkManager.getNetworkObservable(Constants.TMDB)).thenReturn(Observable.just(true));
        Mockito.when(interactor.loadData(2, Constants.POPULAR)).thenReturn(Observable.fromArray(emptyMovies()));

        presenter.loadNext(2);

        Mockito.verify(viewState).addMovies(emptyMovies());
    }

    @Test
    public void movieOnLoadNextError() throws Exception {
        Mockito.when(networkManager.getNetworkObservable(Constants.TMDB)).thenReturn(Observable.just(false));

        presenter.loadNext(2);

        Mockito.verify(viewState).showError();
    }

    @Test
    public void loadRefresh() throws Exception {
        Mockito.when(networkManager.getNetworkObservable(Constants.TMDB)).thenReturn(Observable.just(true));
        Mockito.when(interactor.loadData(1,Constants.POPULAR)).thenReturn(Observable.fromArray(emptyMovies()));

        presenter.loadRefresh();

        Mockito.verify(viewState).showRefreshing();
        Mockito.verify(viewState).hideRefreshing();
        Mockito.verify(viewState).setMovies(emptyMovies());
    }

    @After
    public void tearDown() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    private List<MovieItem> emptyMovies() {
        return new ArrayList<>();
    }
}