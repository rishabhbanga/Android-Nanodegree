package com.innorb.builditbigger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by erishba on 12/24/2017.
 */

@RunWith(AndroidJUnit4.class)
public class EndPointAsyncTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testAsyncResponse() {
        // Find async button to request a joke
        onView(withId(R.id.joke_button)).perform(click());

        // Check if async response is correct, we are considering a non static joke for future purposes
        onView(withId(com.innorb.jokefactory.R.id.joke_text)).check(matches((withText("Become Pun with The Force, Anakin"))));
    }
}