package com.esevinale.myappportfolio.ui.MovieScreen;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.application.AppController;
import com.esevinale.myappportfolio.models.Full;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.utils.manager.NetworkManager;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

@InjectViewState
public class MovieListPresenter extends MvpPresenter<MovieListView> {


    @Inject
    TmdbService tmdbService;

    @Inject
    NetworkManager networkManager;

    private boolean mIsInLoading;

    public MovieListPresenter() {
        AppController.getAppComponent().inject(this);
    }

    public void loadData(ProgressType progressType, int page) {
        if (mIsInLoading) {
            return;
        }
        mIsInLoading = true;

        networkManager.getNetworkObservable()
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

    public Observable<List<MovieItem>> onCreateLoadDataObservable(int page) {
        return tmdbService.getLatestMovie(page).map(Full::getResults)
                .doOnNext(this::saveToDb);
    }

    public Observable<List<MovieItem>> onCreateRestoreDataObservable() {
        return Observable.fromCallable(getListFromRealmCallable());
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


    public void onLoadingStart(ProgressType progressType) {
        showProgress(progressType);
    }

    public void onLoadingFinish(ProgressType progressType) {
        mIsInLoading = false;
        hideProgress(progressType);
    }

    public void onLoadingFailed(Throwable throwable) {
        getViewState().showError(throwable.getMessage());
    }


    public void onLoadingSuccess(ProgressType progressType, List<MovieItem> items) {
        if (progressType == ProgressType.Paging) {
            getViewState().addMovies(items);
        } else {
            getViewState().setMovies(items);
        }
    }


    public enum ProgressType {
        Refreshing, ListProgress, Paging
    }


    public void showProgress(ProgressType progressType) {
        switch (progressType) {
            case Refreshing:
                getViewState().showRefreshing();
                break;
            case ListProgress:
                getViewState().showListProgress();
                break;
        }
    }

    public void hideProgress(ProgressType progressType) {
        switch (progressType) {
            case Refreshing:
                getViewState().hideRefreshing();
                break;
            case ListProgress:
                getViewState().hideListProgress();
                break;
        }
    }

    public void saveToDb(List<MovieItem> item) {
        Realm realm = Realm.getDefaultInstance();
        RealmList<MovieItem> insert = new RealmList<>();
        insert.addAll(item);
        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(insert));
    }

    public Callable<List<MovieItem>> getListFromRealmCallable() {
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
