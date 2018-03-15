package com.esevinale.myappportfolio.ui.MovieDetailsScreen;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.esevinale.myappportfolio.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.esevinale.myappportfolio.ui.MovieDetailsScreen.MovieDetailsActivityTestRule.overview;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MovieDetailsActivityTest {
    @Rule
    public ActivityTestRule<MovieDetailsActivity> activityTestRule
            = new MovieDetailsActivityTestRule(MovieDetailsActivity.class);

    @Test
    public void checkIntent() throws Exception {
        onView(withId(R.id.collapsing_toolbar)).perform(click(), swipeUp());
        onView(withId(R.id.movie_name)).check(matches(withText("(Intent) Zootopia (2016)")));
        onView(withId(R.id.movie_rating)).check(matches(withText("Rating: 7.7")));
        onView(withId(R.id.movie_overview_content)).check(matches(withText(overview)));
        onView(withId(R.id.trailers_section)).check(matches(not(isDisplayed())));
    }

    @Test
    public void homeButtonTest() throws Exception {
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

    @Test
    public void onOptionsItemSelected() throws Exception {
    }

}