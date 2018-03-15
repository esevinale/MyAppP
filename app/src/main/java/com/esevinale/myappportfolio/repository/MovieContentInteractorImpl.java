package com.esevinale.myappportfolio.repository;

import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.models.FullMovie;
import com.esevinale.myappportfolio.models.MovieDTO;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.utils.Constants;
import com.esevinale.myappportfolio.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;


public class MovieContentInteractorImpl implements MovieContentInteractor {

    private TmdbService tmdbService;

    public MovieContentInteractorImpl(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @Override
    public Observable<List<MovieItem>> loadData(int page, int loadType) {
        if (loadType == Constants.LATEST)
            return tmdbService.getLatestMovies(Utils.createGteDate(), Utils.createLteDate(), page)
                    .map(FullMovie::getResults)
                    .map(movieDTOS1 -> movieMapper(movieDTOS1, Constants.LATEST))
                    .doOnNext(this::saveToDb);
        else
            return tmdbService.getPopularMovies(page)
                    .map(FullMovie::getResults)
                    .map(movieDTOS1 -> movieMapper(movieDTOS1, Constants.POPULAR))
                    .doOnNext(this::saveToDb);

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


    private void saveToDb(List<MovieItem> item) {
        Realm realm = Realm.getDefaultInstance();
        RealmList<MovieItem> insert = new RealmList<>();
        insert.addAll(item);
        realm.executeTransaction(realm1 -> {
            realm1.delete(MovieItem.class);
            realm1.insertOrUpdate(insert);
        });
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
