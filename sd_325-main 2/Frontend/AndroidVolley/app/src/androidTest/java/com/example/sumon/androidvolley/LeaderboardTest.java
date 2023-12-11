package com.example.sumon.androidvolley;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LeaderboardTest {

    @Rule
    public ActivityTestRule<LeaderboardActivity> activityRule = new
            ActivityTestRule<>(LeaderboardActivity.class);


    @Test
    public void clickWeekly()
    {
        onView(withId(R.id.btnWeekly)).perform(click());
    }
    @Test
    public void clickLifetime()
    {
        onView(withId(R.id.btnLifetime)).perform(click());
    }
    @Test
    public void clickBacktoDaily()
    {
        onView(withId(R.id.btnWeekly)).perform(click());
        onView(withId(R.id.btnDaily)).perform(click());
    }
    @Test
    public void clickStringRequest() {
        onView(withId(R.id.stringRequestButton)).perform(click());
    }
}