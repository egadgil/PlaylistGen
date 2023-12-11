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
public class SystemTestAvatar {

    @Rule
    public ActivityTestRule<AvatarActivity> activityRule = new
            ActivityTestRule<>(AvatarActivity.class);

    @Test
    public void testClouds() {
        onView(withId(R.id.profile_clouds)).perform(click());
        onView(withId(R.id.set_profile)).perform(click());
        if(!GlobalUser.getAvatar().equals("clouds"))
        {
            onView(withId(R.id.set_profile)).check(matches(withText("hi")));
        }
    }
    @Test
    public void testSwirl() {
        onView(withId(R.id.profile_swirl)).perform(click());
        onView(withId(R.id.set_profile)).perform(click());
        if(!GlobalUser.getAvatar().equals("swirl"))
        {
            onView(withId(R.id.set_profile)).check(matches(withText("hi")));
        }
    }
    @Test
    public void testMoon() {
        onView(withId(R.id.profile_moon)).perform(click());
        onView(withId(R.id.set_profile)).perform(click());
        if(!GlobalUser.getAvatar().equals("moon"))
        {
            onView(withId(R.id.set_profile)).check(matches(withText("hi")));
        }
    }
    @Test
    public void testHeadphone() {
        onView(withId(R.id.profile_headphone)).perform(click());
        onView(withId(R.id.set_profile)).perform(click());
        if(!GlobalUser.getAvatar().equals("headphone"))
        {
            onView(withId(R.id.set_profile)).check(matches(withText("hi")));
        }
    }
    @Test
    public void testBack() {
        onView(withId(R.id.profile)).perform(click());
    }
}
