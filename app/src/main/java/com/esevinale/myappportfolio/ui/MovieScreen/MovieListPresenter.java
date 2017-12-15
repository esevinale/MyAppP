package com.esevinale.myappportfolio.ui.MovieScreen;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.application.AppController;
import com.esevinale.myappportfolio.models.FullMovie;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.utils.Constants;
import com.esevinale.myappportfolio.utils.manager.NetworkManager;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

@InjectViewState
public class MovieListPresenter extends MvpPresenter<MovieListView> {


    @Inject
    TmdbService tmdbService;

    @Inject
    NetworkManager networkManager;

    private boolean mIsInLoading;

    MovieListPresenter() {
        AppController.getAppComponent().inject(this);
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
                            ? onCreateLoadDataObservable(page)
                            : onCreateRestoreDataObservable();

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

    private Observable<List<MovieItem>> onCreateLoadDataObservable(int page) {
        return tmdbService.getLatestMovie(page).map(FullMovie::getResults)
                .doOnNext(this::saveToDb);
    }

    private Observable<List<MovieItem>> onCreateRestoreDataObservable() {
        return Observable.fromCallable(getListFromRealmCallable());
    }


    void loadStart() {
        loadData(ProgressType.ListProgress, 1);
    }

    void loadNext(int page) {
        loadData(ProgressType.Paging, page);
    }

    void loadRefresh() {
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


    public enum ProgressType {
        Refreshing, ListProgress, Paging
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

    private void saveToDb(List<MovieItem> item) {
        Realm realm = Realm.getDefaultInstance();
        RealmList<MovieItem> insert = new RealmList<>();
        insert.addAll(item);
        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(insert));
    }

    private Callable<List<MovieItem>> getListFromRealmCallable() {
        return () -> {
            String[] sorftFields = {"popularity"};
            Sort[] sortOrder = {Sort.DESCENDING};
            Realm realm = Realm.getDefaultInstance();
            RealmResults<MovieItem> realmResults = realm.where(MovieItem.class)
                    .findAllSorted(sorftFields, sortOrder);
            return realm.copyFromRealm(realmResults);
        };
    }
}
