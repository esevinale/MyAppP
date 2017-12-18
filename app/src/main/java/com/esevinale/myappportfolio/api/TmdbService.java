package com.esevinale.myappportfolio.api;


import com.esevinale.myappportfolio.models.FullMovie;
import com.esevinale.myappportfolio.models.FullVideo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbService {

    @GET("3/discover/movie?&language=en-US&sort_by=popularity.desc")
    Observable<FullMovie> getPopularMovies(@Query("page") int page);

    @GET("3/discover/movie?&language=en-US&sort_by=popularity.desc")
    Observable<FullMovie> getLatestMovies(@Query("primary_release_date.gte")String gte, @Query("primary_release_date.lte") String lte, @Query("page") int page);

    @GET("3/movie/{movie_id}/videos")
    Observable<FullVideo> getYoutubeFrailers(@Path("movie_id") int movie_id);
}
