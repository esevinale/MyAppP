package com.esevinale.myappportfolio.ui.MovieScreen;


import com.esevinale.myappportfolio.models.MovieItem;

import java.util.List;

import io.reactivex.Observable;

public interface MovieContentInteractor {
    Observable<List<MovieItem>> loadData(int page);
    Observable<List<MovieItem>> restoreData();
}
