package com.esevinale.myappportfolio.ui.MovieDetailsScreen;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.esevinale.myappportfolio.R;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.ui.BaseFragment;
import com.esevinale.myappportfolio.utils.Constants;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends MvpAppCompatFragment {

    Unbinder unbinder;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
