package com.esevinale.myappportfolio.ui.MovieDetailsScreen;

import android.os.Bundle;
import android.util.Log;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.esevinale.myappportfolio.R;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.utils.Constants;

public class MovieDetailsActivity extends MvpAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.containsKey(Constants.MOVIE_ITEM)) {
                MovieItem movie = extras.getParcelable(Constants.MOVIE_ITEM);
                if (movie != null) {
                    MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.getInstance(movie);
                    getSupportFragmentManager().beginTransaction().add(R.id.details_wrapper, movieDetailsFragment).commit();
                    Log.d("myLogs", "onCreate: 123");
                }
            }
        }
    }
}
