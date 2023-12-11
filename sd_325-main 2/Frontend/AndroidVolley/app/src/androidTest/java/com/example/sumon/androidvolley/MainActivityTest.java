package com.example.sumon.androidvolley;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
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
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new
            ActivityTestRule<>(MainActivity.class);
    @Test
    public void testLogin() {
        onView(withId(R.id.btnLogin)).perform(click());
        getClass().equals(LoginActivity.class);
    }
    @Test
    public void testSignup() {
        onView(withId(R.id.btnSignup)).perform(click());
        getClass().equals(SignupActivity.class);
    }
    @Test
    public void testAdmin() {
        onView(withId(R.id.btnAdmin)).perform(click());
        getClass().equals(AdminDashActivity.class);
    }
    @Test
    public void testClick() {
        onView(withId(R.id.welcomeSign)).perform(click());
        getClass().equals(MainActivity.class);
    }

}
