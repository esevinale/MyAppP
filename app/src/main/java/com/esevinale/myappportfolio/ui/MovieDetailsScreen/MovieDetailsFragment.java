package com.esevinale.myappportfolio.ui.MovieDetailsScreen;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esevinale.myappportfolio.R;
import com.esevinale.myappportfolio.api.ApiConstants;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.models.Video;
import com.esevinale.myappportfolio.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailsFragment extends MvpAppCompatFragment implements MovieDetailsView {

    private Unbinder unbinder;
    @BindView(R.id.movie_name)
    TextView movieName;
    @BindView(R.id.movie_rating)
    TextView movieRate;
    @BindView(R.id.movie_overview_content)
    TextView movieOverview;
    @BindView(R.id.movie_poster)
    ImageView moviePoster;
    @BindView(R.id.collapsedToolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.trailers_section)
    LinearLayout trailerSection;
    @BindView(R.id.trailers_container)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.ll_trailers)
    LinearLayout trailersll;

    @InjectPresenter
    MovieDetailsPresenterImpl mPresenter;


    public MovieDetailsFragment() {
        // Required empty public constructor
    }


    public static MovieDetailsFragment getInstance(@NonNull MovieItem movie) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.MOVIE_ITEM, movie);
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setArguments(args);
        return movieDetailsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setUpToolBar();
        return rootView;
    }

    private void setUpToolBar() {
        if (toolbar != null) {
            if (getActivity() != null) {
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            }

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }



    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            MovieItem movieItem = (MovieItem) getArguments().get(Constants.MOVIE_ITEM);
            if (movieItem != null)
                mPresenter.movieItemReady(movieItem);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showMovieDetails(MovieItem movieItem) {
        collapsingToolbar.setTitle(String.format(getString(R.string.movie_details_toolbar), movieItem.getTitle()));
        movieName.setText(String.format(getString(R.string.movie), movieItem.getTitle(), movieItem.getReleaseDate().substring(0, 4)));
        movieRate.setText(String.format(getString(R.string.movie_rating), String.valueOf(movieItem.getVoteAverage())));
        movieOverview.setText(movieItem.getOverview());
    }

    @Override
    public void showMoviePoster(String path) {
        Glide
                .with(getContext())
                .load(path)
                .into(moviePoster);
    }

    @Override
    public void showTrailers(List<Video> video) {
        trailerSection.setVisibility(View.VISIBLE);

        this.trailersll.removeAllViews();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        RequestOptions options = new RequestOptions()
                .placeholder(R.color.colorPrimary)
                .centerCrop()
                .override(150, 150);

        for (Video trailer : video) {
            View thumbContainer = inflater.inflate(R.layout.video_model, this.trailersll, false);
            ImageView thumbView = ButterKnife.findById(thumbContainer, R.id.video_poster);
            thumbView.requestLayout();
            thumbView.setOnClickListener(view -> mPresenter.onTrailerClicked(ApiConstants.BASE_YOUTUBE_URL + "?v=" + trailer.getKey()));
            Glide.with(getContext())
                    .load(String.format(ApiConstants.POSTER_YOUTUBE_URL, trailer.getKey()))
                    .apply(options)
                    .into(thumbView);
            this.trailersll.addView(thumbContainer);
        }
    }

    @Override
    public void startTrailerIntent(String url) {
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(playVideoIntent);
    }
}
