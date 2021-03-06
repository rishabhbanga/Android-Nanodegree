package com.innorb.recipeapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.innorb.recipeapp.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void checkFirstText_RecipeActivity() {
        onView(withId(R.id.rv_recipe))
                .perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));
    }

    @Test
    public void checkLastText_RecipeActivity() {
        onView(withId(R.id.rv_recipe))
                .perform(RecyclerViewActions.scrollToPosition(3));
        onView(withText("Cheesecake")).check(matches(isDisplayed()));
    }

    @Test
    public void checkFirstContentDescription_RecipeActivity() {
        onView(withId(R.id.rv_recipe))
                .perform(RecyclerViewActions.scrollToPosition(0));
        onView(withContentDescription("Nutella Pie")).check(matches(isDisplayed()));
    }

    @Test
    public void checkLastContentDescription_RecipeActivity() {
        onView(withId(R.id.rv_recipe))
                .perform(RecyclerViewActions.scrollToPosition(3));
        onView(withContentDescription("Cheesecake")).check(matches(isDisplayed()));
    }

    @Test
    public void checkFirstIngredient_DetailActivity() {
        onView(withId(R.id.rv_recipe))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0, click()));
        onView(withText("Graham cracker crumbs"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkFirstClickIngredient_DetailActivity() {
        onView(withId(R.id.rv_recipe))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rv_detail_ingredient))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(3, click()));
        onView(withText("Salt"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkTitle_StepActivity() {
        onView(withId(R.id.rv_recipe))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rv_step))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0, click()));
        onView(withId(R.id.tv_short_detail_step_description))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}