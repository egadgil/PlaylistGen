package com.example.sumon.androidvolley;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
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
public class DiscoverPageTest {

    @Rule
    public ActivityTestRule<DiscoverPage> activityRule = new
            ActivityTestRule<>(DiscoverPage.class);

    @Test
    public void  clickTopTenBtn() {
        onView(withId(R.id.toptenBtn)).perform(click());
    }
    @Test
    public void clickAllBtn() {
        onView(withId(R.id.allBtn)).perform(click());
    }
}
