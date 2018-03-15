package com.esevinale.myappportfolio.ui.MovieListScreen;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.esevinale.myappportfolio.R;
import com.esevinale.myappportfolio.models.MovieItem;
import com.esevinale.myappportfolio.test.assertions.RecyclerViewItemCountAssertion;
import com.esevinale.myappportfolio.ui.MovieDetailsScreen.MovieDetailsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.realm.Realm;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MovieListActivityTest {
    @Rule
    public ActivityTestRule<MovieListActivity> activityTestRule
            = new ActivityTestRule<>(MovieListActivity.class);


    @Before
    public void init() {
        Intents.init();
    }

    @Test
    public void checkRecyclerInit() throws Exception {
        onView(withId(R.id.rv_movie)).check(matches(allOf(
                isDisplayed(),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                isFocusable()
        )));
    }

    @Test
    public void testScrollRecyclerView() throws Exception {
        onView(withId(R.id.rv_movie))
                .perform(scrollToPosition(15))
                .perform(scrollToPosition(8))
                .perform(scrollToPosition(1))
                .perform(scrollToPosition(2))
                .perform(scrollToPosition(10))
                .perform(scrollToPosition(19));
    }

    @Test
    public void onMenuItemClick() throws Exception {
        SystemClock.sleep(500);
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.action_sort_popularity)).perform(click());
        onView(withId(R.id.rv_movie)).check(new RecyclerViewItemCountAssertion(20));

        openActionBarOverflowOrOptionsMenu(activityTestRule.getActivity());
        onView(withText(R.string.action_sort_latest)).perform(click());
        onView(withId(R.id.rv_movie)).check(new RecyclerViewItemCountAssertion(20));
    }

    @Test
    public void onMovieClick() throws Exception {
        onView(withId(R.id.rv_movie)).perform(actionOnItemAtPosition(10, click()));
        Intents.intended(hasComponent(MovieDetailsActivity.class.getName()));
    }

    @Test
    public void realmCheck() throws Exception {
        int savedCount = Realm.getDefaultInstance()
                .where(MovieItem.class)
                .findAll()
                .size();
        assertEquals(20, savedCount);
    }


    @After
    public void tearDown() throws Exception {
        Intents.release();
        Realm.getDefaultInstance().executeTransaction(realm -> realm.deleteAll());
    }
}