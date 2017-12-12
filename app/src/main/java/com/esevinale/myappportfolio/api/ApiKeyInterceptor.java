package com.esevinale.myappportfolio.api;


import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.esevinale.myappportfolio.api.ApiConstants.TMDB_KEY;

@Singleton
public class ApiKeyInterceptor implements Interceptor{

    @Inject
    public ApiKeyInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        HttpUrl suppUrl = chain.request().url().newBuilder()
                .addQueryParameter("api_key", TMDB_KEY)
                .build();

        Request suppRequest = request.newBuilder().url(suppUrl).build();
        return chain.proceed(suppRequest);
    }
}
