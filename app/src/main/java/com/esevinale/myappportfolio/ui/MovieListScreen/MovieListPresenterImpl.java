package com.esevinale.myappportfolio.ui.MovieListScreen;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.repository.MovieContentInteractor;
import com.esevinale.myappportfolio.MyIdlingResources;
import com.esevinale.myappportfolio.utils.Constants;
import com.esevinale.myappportfolio.utils.manager.MyPreferencesManager;
import com.esevinale.myappportfolio.utils.manager.NetworkManager;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MovieListPresenterImpl extends MvpPresenter<MovieListView> implements MovieListPresenter {


    private MovieContentInteractor movieContentInteractor;

    private NetworkManager networkManager;

    private MyPreferencesManager preferencesManager;

    private MyIdlingResources resources;
    private boolean mIsInLoading;

    private static byte loadType;

    public MovieListPresenterImpl(MovieContentInteractor movieContentInteractor, NetworkManager networkManager, MyPreferencesManager preferencesManager, MyIdlingResources resources) {
        this.movieContentInteractor = movieContentInteractor;
        this.networkManager = networkManager;
        this.preferencesManager = preferencesManager;
        loadType = (byte) preferencesManager.getSelectedOption();
        this.resources = resources;
    }

    private void loadData(ProgressType progressType, int page) {
        if (mIsInLoading) {
            return;
        }
        mIsInLoading = true;
        resources.loadStart();
        networkManager.getNetworkObservable(Constants.TMDB)
                .flatMap(aBoolean -> {
                    if (!aBoolean && page > 1) {
                        return Observable.error(new IOException("No Connection"));
                    }
                    return aBoolean
                            ? movieContentInteractor.loadData(page, loadType)
                            : movieContentInteractor.restoreData(loadType);

                })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showProgress(progressType))
                .doFinally(() -> {
                    hideProgress(progressType);
                    resources.loadFinish();
                })
                .subscribe(movies -> onLoadingSuccess(progressType, movies), error -> {
                    onLoadingFailed();
                });
    }

    public void loadStart() {
        loadData(ProgressType.ListProgress, 1);
    }

    public void loadNext(int page) {
        loadData(ProgressType.Paging, page);
    }

    public void loadRefresh() {
        loadData(ProgressType.Refreshing, 1);
    }


    private void showProgress(ProgressType progressType) {
        switch (progressType) {
            case Refreshing:
                getViewState().showRefreshing();
                break;
            case ListProgress:
                getViewState().showListProgress();
                break;
        }
    }

    private void hideProgress(ProgressType progressType) {
        mIsInLoading = false;
        switch (progressType) {
            case Refreshing:
                getViewState().hideRefreshing();
                break;
            case ListProgress:
                getViewState().hideListProgress();
                break;
        }
    }

    private void onLoadingFailed() {
        getViewState().showError();
    }

    private void onLoadingSuccess(ProgressType progressType, List<MovieItem> items) {
        if (progressType == ProgressType.Paging) {
            getViewState().addMovies(items);
        } else {
            getViewState().setMovies(items);
        }
    }

    public void onOptionItemSelected(byte loadType) {
        MovieListPresenterImpl.loadType = loadType;
        preferencesManager.setSelectedOption(loadType);
        loadStart();
    }

    @Override
    public void onMovieClicked(MovieItem movieItem) {
        getViewState().startMovieDetails(movieItem);
    }
}