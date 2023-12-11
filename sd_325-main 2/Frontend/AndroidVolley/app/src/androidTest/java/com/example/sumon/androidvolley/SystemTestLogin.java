package com.example.sumon.androidvolley;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
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
public class SystemTestLogin {

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new
            ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testProgressDialogAndLogin() {
        onView(withId(R.id.loginEnterBtn)).perform(click());

    }
    @Test
    public void testLogin() throws InterruptedException {
        onView(withId(R.id.usernameText)).perform(typeText("v"));
        onView(withId(R.id.passwordText)).perform(typeText("zlovesv"));
        onView(withId(R.id.loginEnterBtn)).perform(click());
        getClass().equals(UserDashboardActivity.class);
    }

}
