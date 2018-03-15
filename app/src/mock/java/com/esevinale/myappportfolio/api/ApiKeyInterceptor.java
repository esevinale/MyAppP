package com.esevinale.myappportfolio.api;


import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.esevinale.myappportfolio.application.AppController;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.esevinale.myappportfolio.api.ApiConstants.TMDB_KEY;

@Singleton
public class ApiKeyInterceptor implements Interceptor{

    private Context context;

    @Inject
    ApiKeyInterceptor(Context context) {
        this.context = context;
    }


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        return createResponseFromAssets(request);
    }

    @NonNull
    private Response createResponseFromAssets(@NonNull Request request) {
        try {
            final InputStream stream = context.getAssets().open("imdb.json");
            //noinspection TryFinallyCanBeTryWithResources
            try {
                return OkHttpResponse.success(request, stream);
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            return OkHttpResponse.error(request, 500, e.getMessage());
        }
    }
}
