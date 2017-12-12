package com.esevinale.myappportfolio.api;


import com.esevinale.myappportfolio.models.Full;
import com.esevinale.myappportfolio.models.FullMovie;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbService {

    @GET("3/discover/movie?&sort_by=popularity.desc&language=en-US")
    Observable<Response<Full>> getLatestMovie();
}
