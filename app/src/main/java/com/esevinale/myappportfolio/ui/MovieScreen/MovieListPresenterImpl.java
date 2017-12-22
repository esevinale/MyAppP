package com.esevinale.myappportfolio.ui.MovieScreen;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.esevinale.myappportfolio.application.AppController;
import com.esevinale.myappportfolio.application.builder.MovieListModule;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.utils.Constants;
import com.esevinale.myappportfolio.utils.manager.MyPreferencesManager;
import com.esevinale.myappportfolio.utils.manager.NetworkManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MovieListPresenterImpl extends MvpPresenter<MovieListView> implements MovieListPresenter {


    @Inject
    MovieContentInteractorImpl movieContentInteractor;

    @Inject
    NetworkManager networkManager;

    @Inject
    MyPreferencesManager preferencesManager;

    private boolean mIsInLoading;

    static byte loadType;

    MovieListPresenterImpl() {
        AppController.getAppComponent().createMovieListComponent(new MovieListModule()).inject(this);
        loadType = (byte) preferencesManager.getSelectedOption();
    }

    private void loadData(ProgressType progressType, int page) {
        if (mIsInLoading) {
            return;
        }
        mIsInLoading = true;

        networkManager.getNetworkObservable(Constants.TMDB)
                .flatMap(aBoolean -> {
                    if (!aBoolean && page > 1) {
                        return Observable.empty();
                    }
                    return aBoolean
                            ? movieContentInteractor.loadData(page)
                            : movieContentInteractor.restoreData();

                })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> onLoadingStart(progressType))
                .doFinally(() -> onLoadingFinish(progressType))
                .subscribe(movies -> onLoadingSuccess(progressType, movies), error -> {
                    error.printStackTrace();
                    onLoadingFailed(error);
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


    private void onLoadingStart(ProgressType progressType) {
        showProgress(progressType);
    }

    private void onLoadingFinish(ProgressType progressType) {
        mIsInLoading = false;
        hideProgress(progressType);
    }

    private void onLoadingFailed(Throwable throwable) {
        getViewState().showError(throwable.getMessage());
    }

    private void onLoadingSuccess(ProgressType progressType, List<MovieItem> items) {
        if (progressType == ProgressType.Paging) {
            getViewState().addMovies(items);
        } else {
            getViewState().setMovies(items);
        }
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
        switch (progressType) {
            case Refreshing:
                getViewState().hideRefreshing();
                break;
            case ListProgress:
                getViewState().hideListProgress();
                break;
        }
    }

    public void onOptionItemSelected(byte loadType) {
        MovieListPresenterImpl.loadType = loadType;
        preferencesManager.setSelectedOption(loadType);
        loadStart();
    }
}