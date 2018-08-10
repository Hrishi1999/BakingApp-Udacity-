package gridentertainment.net.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class SimpleUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);

    private CountingIdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
        }

    @Test
    public void testingRecyclerView() {

        onView(withRecyclerView(R.id.recyclerViewMain).atPositionOnView(0, R.id.rec_item_title))
                .check(matches(withText("Nutella Pie - 8 servings")));

        onView(withId(R.id.recyclerViewMain))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                        click()));

        onView(withId(R.id.recyclerViewIng)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerViewSteps)).check(matches(isDisplayed()));


        onView(withId(R.id.recyclerViewSteps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
