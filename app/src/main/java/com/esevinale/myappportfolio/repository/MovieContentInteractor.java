package com.esevinale.myappportfolio.repository;


import com.esevinale.myappportfolio.models.MovieItem;

import java.util.List;

import io.reactivex.Observable;

public interface MovieContentInteractor {
    Observable<List<MovieItem>> loadData(int page, int loadtype);
    Observable<List<MovieItem>> restoreData(byte filter);
}
