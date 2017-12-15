package com.esevinale.myappportfolio.api;


import com.esevinale.myappportfolio.models.FullMovie;
import com.esevinale.myappportfolio.models.FullVideo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbService {

    @GET("3/discover/movie?&sort_by=popularity.desc&language=en-US")
    Observable<FullMovie> getLatestMovie(@Query("page") int page);

    @GET("3/movie/{movie_id}/videos")
    Observable<FullVideo> getYoutubeFrailers(@Path("movie_id") int movie_id);
}
