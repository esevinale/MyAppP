package com.esevinale.myappportfolio.ui.MovieScreen;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.esevinale.myappportfolio.R;
import com.esevinale.myappportfolio.api.ApiConstants;
import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.application.AppController;
import com.esevinale.myappportfolio.models.Full;
import com.esevinale.myappportfolio.models.FullMovie;
import com.esevinale.myappportfolio.models.Movie;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListFragment extends BaseFragment implements MovieListView{

    @Inject
    TmdbService tmdbService;

    RecyclerView mRecyclerView;
    MovieListAdapter movieListAdapter;

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.getAppComponent().inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView(view);
        setUpAdapter(mRecyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tmdbService.getLatestMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<Full>>() {
                    @Override
                    public void accept(Response<Full> fullResponse) throws Exception {
                        Log.d("myLogs", "REQUEST: " + fullResponse.raw().request().url().toString());

                        List<MovieItem> movies = new ArrayList<>();
                        for (MovieItem movie : fullResponse.body().getResults())
                            movies.add(movie);

                        movieListAdapter.setMovies(movies);
                    }
                });
    }

    @Override
    protected int getMainContentLayout() {
        return R.layout.fragment_movie;
    }

    @Override
    public int onCreateToolbarTitle() {
        return R.string.movie_screen_name;
    }

    private void setUpRecyclerView (View rootView) {
        mRecyclerView = rootView.findViewById(R.id.rv_movie);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

    private void setUpAdapter(RecyclerView recyclerView) {
        movieListAdapter = new MovieListAdapter(this);
        recyclerView.setAdapter(movieListAdapter);
    }

    @Override
    public void showMovies(List<MovieItem> movies) {

    }

    @Override
    public void loadingStarted() {

    }

    @Override
    public void loadingFailed(String errorMessage) {

    }

    @Override
    public void onMovieClicked(MovieItem movie) {

    }
}
