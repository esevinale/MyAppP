package com.esevinale.myappportfolio.ui.MovieListScreen;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.esevinale.myappportfolio.R;
import com.esevinale.myappportfolio.application.AppController;
import com.esevinale.myappportfolio.application.builder.MovieListComponent;
import com.esevinale.myappportfolio.application.builder.MovieListModule;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.ui.BaseFragment;
import com.esevinale.myappportfolio.ui.MovieDetailsScreen.MovieDetailsActivity;
import com.esevinale.myappportfolio.utils.Constants;
import com.esevinale.myappportfolio.utils.manager.MyGridLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieListFragment extends BaseFragment implements MovieListView {

    @BindView(R.id.rv_movie)
    RecyclerView mRecyclerView;

    @BindView(R.id.movie_swipe)
    protected SwipeRefreshLayout mSwipe;
    protected ProgressBar mProgressBar;

    @InjectPresenter
    MovieListPresenterImpl movieListPresenter;

    @ProvidePresenter
    MovieListPresenterImpl providePresenter() {
        return movieListComponent.providePresenter();
    }

    private MovieListAdapter movieListAdapter;
    private Unbinder unbinder;
    private MovieListComponent movieListComponent;

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        movieListComponent = AppController.getAppComponent().createMovieListComponent(new MovieListModule());
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popularity:
                movieListPresenter.onOptionItemSelected(Constants.POPULAR);
                break;
            case R.id.action_sort_latest:
                movieListPresenter.onOptionItemSelected(Constants.LATEST);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getMainContentLayout() {
        return R.layout.fragment_movie_list;
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
                    movieListPresenter.loadNext((movieListAdapter.getItemCount() / 20) + 1);
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
        mSwipe.setRefreshing(true);
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
    public void showError() {
        Toast.makeText(getBaseActivity(), getString(R.string.loadingError), Toast.LENGTH_LONG).show();
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
        movieListPresenter.onMovieClicked(movie);
    }

    @Override
    public void startMovieDetails(MovieItem movie) {
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.MOVIE_ITEM, movie);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
