package com.esevinale.myappportfolio.api;


import com.esevinale.myappportfolio.models.Full;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbService {

    @GET("3/discover/movie?&sort_by=popularity.desc&language=en-US")
    Observable<Full> getLatestMovie(@Query("page") int page);
}
