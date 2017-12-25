package com.esevinale.myappportfolio.ui.MovieScreen;

import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.models.FullMovie;
import com.esevinale.myappportfolio.models.MovieDTO;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.esevinale.myappportfolio.ui.MovieScreen.MovieListPresenterImpl.loadType;

public class MovieContentInteractorImpl implements MovieContentInteractor {

    private TmdbService tmdbService;

    public MovieContentInteractorImpl(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @Override
    public Observable<List<MovieItem>> loadData(int page) {
        switch (loadType) {
            case Constants.POPULAR:
                return tmdbService.getPopularMovies(page)
                        .map(FullMovie::getResults)
                        .map(movieDTOS1 -> movieMapper(movieDTOS1, Constants.POPULAR))
                        .doOnNext(this::saveToDb);
            case Constants.LATEST:
                return tmdbService.getLatestMovies(createGteDate(), createLteDate(), page)
                        .map(FullMovie::getResults)
                        .map(movieDTOS1 -> movieMapper(movieDTOS1, Constants.LATEST))
                        .doOnNext(this::saveToDb);
            default:
                return tmdbService.getPopularMovies(1)
                        .map(FullMovie::getResults)
                        .map(movieDTOS1 -> movieMapper(movieDTOS1, Constants.POPULAR))
                        .doOnNext(this::saveToDb);
        }
    }

    @Override
    public Observable<List<MovieItem>> restoreData(byte filter) {
        return Observable.fromCallable(getListFromRealmCallable(filter));
    }

    private List<MovieItem> movieMapper(List<MovieDTO> movieDTOS, byte filter) {
        List<MovieItem> movies = new ArrayList<>();
        for (MovieDTO movieDTO : movieDTOS)
            movies.add(new MovieItem(movieDTO.getId(), movieDTO.getVideo(), movieDTO.getVoteAverage(), movieDTO.getTitle(), movieDTO.getPopularity(), movieDTO.getPosterPath(), movieDTO.getBackdropPath(), movieDTO.getOverview(), movieDTO.getReleaseDate(), filter));
        return movies;
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

    private void saveToDb(List<MovieItem> item) {
        Realm realm = Realm.getDefaultInstance();
        RealmList<MovieItem> insert = new RealmList<>();
        insert.addAll(item);
        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(insert));
    }

    private Callable<List<MovieItem>> getListFromRealmCallable(byte filter) {
        return () -> {
            String[] sortFields = {"popularity"};
            Sort[] sortOrder = {Sort.DESCENDING};
            Realm realm = Realm.getDefaultInstance();
            RealmResults<MovieItem> realmResults;
            if (filter == Constants.LATEST)
                realmResults = realm.where(MovieItem.class)
                        .equalTo("filter_type", Constants.LATEST)
                        .findAllSorted(sortFields, sortOrder);
            else
                realmResults = realm.where(MovieItem.class)
                        .equalTo("filter_type", Constants.POPULAR)
                        .findAllSorted(sortFields, sortOrder);
            return realm.copyFromRealm(realmResults);
        };
    }
}
