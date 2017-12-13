package com.esevinale.myappportfolio.utils.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.esevinale.myappportfolio.api.ApiConstants;
import com.esevinale.myappportfolio.application.AppController;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.realm.internal.Util;

public class NetworkManager {
    @Inject
    Context mContext;

    private static final String TAG = "NetworkManager";

    public NetworkManager() {
        AppController.getAppComponent().inject(this);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return ((networkInfo != null && networkInfo.isConnected()) || Util.isEmulator());
    }

    public Callable<Boolean> isTmdbReachableCallable() {
        return () -> {
            try {
                if (!isOnline()) {
                    return false;
                }

                URL url = new URL(ApiConstants.BASE_TMDB_URL);
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(2000);
                urlc.connect();

                return true;

            } catch (Exception e) {
                return false;
            }
        };
    }


    public Observable<Boolean> getNetworkObservable() {
        return Observable.fromCallable(isTmdbReachableCallable());
    }
}