package com.esevinale.myappportfolio.ui.MovieScreen;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.esevinale.myappportfolio.R;
import com.esevinale.myappportfolio.api.TmdbService;
import com.esevinale.myappportfolio.application.AppController;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.ui.BaseFragment;
import com.esevinale.myappportfolio.utils.manager.MyGridLayoutManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieListFragment extends BaseFragment implements MovieListView {

    @Inject
    TmdbService tmdbService;

    @BindView(R.id.rv_movie)
    RecyclerView mRecyclerView;

    @BindView(R.id.movie_swipe)
    protected SwipeRefreshLayout mSwipe;
    protected ProgressBar mProgressBar;

    @InjectPresenter
    MovieListPresenter movieListPresenter;

    MovieListAdapter movieListAdapter;
    private Unbinder unbinder;

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
        unbinder = ButterKnife.bind(this, view);
        setUpSwipeToRefreshLayout();
        setUpRecyclerView();
        setUpAdapter(mRecyclerView);
        movieListPresenter.loadStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected int getMainContentLayout() {
        return R.layout.fragment_movie;
    }

    @Override
    public int onCreateToolbarTitle() {
        return R.string.movie_screen_name;
    }

    private void setUpRecyclerView() {
        MyGridLayoutManager myGridLayoutManager = new MyGridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(myGridLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (myGridLayoutManager.isOnNextPagePosition())
                    movieListPresenter.loadNext((movieListAdapter.getItemCount()/20) + 1);
            }
        });

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void setUpAdapter(RecyclerView recyclerView) {
        movieListAdapter = new MovieListAdapter(this);
        recyclerView.setAdapter(movieListAdapter);
    }

    private void setUpSwipeToRefreshLayout() {
        mSwipe.setOnRefreshListener(() -> movieListPresenter.loadRefresh());
        mSwipe.setColorSchemeResources(R.color.colorAccent);
        mProgressBar = getBaseActivity().getmProgressBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showRefreshing() {

    }

    @Override
    public void hideRefreshing() {
        mSwipe.setRefreshing(false);
    }

    @Override
    public void showListProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideListProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getBaseActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setMovies(List<MovieItem> movies) {
        movieListAdapter.setMovies(movies);
    }

    @Override
    public void addMovies(List<MovieItem> movies) {
        movieListAdapter.addMovies(movies);
    }

    @Override
    public void onMovieClicked(MovieItem movie) {

    }
}
