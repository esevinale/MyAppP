package com.esevinale.myappportfolio.utils.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.esevinale.myappportfolio.api.ApiConstants;
import com.esevinale.myappportfolio.application.AppController;
import com.esevinale.myappportfolio.utils.Constants;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.realm.internal.Util;

public class NetworkManager {
    @Inject
    Context mContext;

    public NetworkManager() {
        AppController.getAppComponent().inject(this);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return ((networkInfo != null && networkInfo.isConnected()) || Util.isEmulator());
    }

    private Callable<Boolean> isTmdbReachableCallable(String site) {
        return () -> {
            try {
                if (!isOnline()) {
                    return false;
                }

                URL url = new URL(ApiConstants.BASE_TMDB_URL);
                if (site.equals(Constants.YOUTUBE))
                    url = new URL(ApiConstants.BASE_YOUTUBE_URL);
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(2000);
                urlc.connect();

                return true;

            } catch (Exception e) {
                return false;
            }
        };
    }


    public Observable<Boolean> getNetworkObservable(String site) {
        return Observable.fromCallable(isTmdbReachableCallable(site));
    }
}