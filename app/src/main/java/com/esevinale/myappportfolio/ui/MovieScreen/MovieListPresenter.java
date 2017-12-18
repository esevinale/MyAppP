package com.esevinale.myappportfolio.ui.MovieScreen;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.esevinale.myappportfolio.api.ApiConstants;
import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.application.AppController;
import com.esevinale.myappportfolio.models.FullMovie;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.utils.Constants;
import com.esevinale.myappportfolio.utils.manager.MyPreferencesManager;
import com.esevinale.myappportfolio.utils.manager.NetworkManager;

import java.util.Calendar;
import java.util.Date;
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

    @Inject
    MyPreferencesManager preferencesManager;

    private boolean mIsInLoading;

    private static byte loadType;

    MovieListPresenter() {
        AppController.getAppComponent().inject(this);
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
        switch (loadType) {
            case Constants.POPULAR:
                return tmdbService.getPopularMovies(page).map(FullMovie::getResults)
                        .doOnNext(this::saveToDb);
            case Constants.LATEST:
                return tmdbService.getLatestMovies(createGteDate(), createLteDate(), page).map(FullMovie::getResults)
                        .doOnNext(this::saveToDb);
            default:
                return tmdbService.getPopularMovies(1).map(FullMovie::getResults)
                        .doOnNext(this::saveToDb);
        }
    }

    private String createGteDate() {
        int monthGte = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return primaryReleaseDateToString(monthGte, Calendar.getInstance().get(Calendar.YEAR));
    }
    private String createLteDate() {
        int monthLte = Calendar.getInstance().get(Calendar.MONTH) + 2;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if (monthLte > 12)
            return primaryReleaseDateToString(1, year + 1);
        else
            return primaryReleaseDateToString(monthLte, year);
    }

    private String primaryReleaseDateToString(int monthInt, int year) {
        String month = Integer.toString(monthInt);
        if (month.length() == 1)
            month = "0".concat(month);
        return Integer.toString(year) + "-" + month + "-01";
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
            String[] sortFields = {"popularity"};
            Sort[] sortOrder = {Sort.DESCENDING};
            if (loadType == Constants.LATEST)
                sortFields = new String[]{"releaseDate"};
            Realm realm = Realm.getDefaultInstance();
            RealmResults<MovieItem> realmResults = realm.where(MovieItem.class)
                    .findAllSorted(sortFields, sortOrder);
            return realm.copyFromRealm(realmResults);
        };
    }

    void onOptionItemSelected(byte loadType) {
        this.loadType = loadType;
        preferencesManager.setSelectedOption(loadType);
        loadStart();
    }
}
