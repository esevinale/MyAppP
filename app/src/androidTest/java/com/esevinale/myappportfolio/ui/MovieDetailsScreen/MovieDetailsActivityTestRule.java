package com.esevinale.myappportfolio.ui.MovieDetailsScreen;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.utils.Constants;

public class MovieDetailsActivityTestRule extends ActivityTestRule<MovieDetailsActivity> {
    public MovieDetailsActivityTestRule(Class<MovieDetailsActivity> activityClass) {
        super(activityClass);
    }

    public static final String overview = "Determined to prove herself, Officer Judy Hopps, the first bunny on Zootopia's police force, jumps at the chance to crack her" +
            " first case - even if it means partnering with scam-artist fox Nick Wilde to solve the mystery.";
    @Override
    protected Intent getActivityIntent() {
        Intent intent = new Intent();
        MovieItem movieItem = new MovieItem(269149, false, 7.7, "(Intent) Zootopia", 481.992544, "sM33SANp9z6rXW8Itn7NnG1GOEs.jpg",
                "\\/mhdeE1yShHTaDbJVdWyTlzFvNkr.jpg", overview, "2016-02-11", Constants.POPULAR);
        intent.putExtra(Constants.MOVIE_ITEM, movieItem);
        return intent;
    }
}
