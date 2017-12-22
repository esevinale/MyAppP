package com.esevinale.myappportfolio.application.builder;

import com.esevinale.myappportfolio.api.ApiConstants;
import com.esevinale.myappportfolio.api.ApiKeyInterceptor;
import com.esevinale.myappportfolio.api.TmdbService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class RestModule {

    private static final int CONNECT_TIMEOUT = 15000;

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(ApiKeyInterceptor requestInterceptor) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new okhttp3.OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(requestInterceptor)
                .build();
    }

    @Singleton
    @Provides
    Retrofit retrofit(OkHttpClient okHttpClient) {
        return new Retrofit
                .Builder()
                .baseUrl(ApiConstants.BASE_TMDB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    TmdbService tmdbWebService(Retrofit retrofit) {
        return retrofit.create(TmdbService.class);
    }
}
