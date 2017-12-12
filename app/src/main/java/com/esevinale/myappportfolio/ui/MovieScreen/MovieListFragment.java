package com.esevinale.myappportfolio.ui.MovieScreen;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.esevinale.myappportfolio.R;
import com.esevinale.myappportfolio.api.ApiConstants;
import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.application.AppController;
import com.esevinale.myappportfolio.models.Full;
import com.esevinale.myappportfolio.models.FullMovie;
import com.esevinale.myappportfolio.ui.BaseFragment;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListFragment extends BaseFragment {

    @Inject
    TmdbService tmdbService;

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.getAppComponent().inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tmdbService.getLatestMovie().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<Full>>() {
                    @Override
                    public void accept(Response<Full> fullResponse) throws Exception {
                Log.d("myLogs", "REQUEST: " + fullResponse.raw().request().url().toString());
                Toast.makeText(getActivity(), "Count: " + fullResponse.body().getTotalResults(), Toast.LENGTH_LONG).show();
                    }
                });
        Observable<Response<Full>> call = tmdbService.getLatestMovie();
    }

    @Override
    protected int getMainContentLayout() {
        return R.layout.fragment_movie;
    }

    @Override
    public int onCreateToolbarTitle() {
        return R.string.movie_screen_name;
    }

}
