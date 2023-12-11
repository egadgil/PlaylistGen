package com.example.sumon.androidvolley;

import static android.service.autofill.Validators.not;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.init;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.IsInstanceOf.instanceOf;

import static java.util.EnumSet.allOf;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.service.autofill.Validator;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserDashboardTest {

    @Rule
    public ActivityTestRule<UserDashboardActivity> activityRule = new
            ActivityTestRule<>(UserDashboardActivity.class);

    @Before
    public void setupIntents() {
        init();
    }
    @After
    public void teardownIntents() {
        release();
    }


    @Test
    public void clickPlaylistGenerator() throws InterruptedException {
        Thread.sleep(6000);
        onView(withId(R.id.playlistGeneratorPageBtn)).perform(click());
        getClass().equals(PlaylistGenerator.class);
    }

    @Test
    public void clickProfilePage() throws InterruptedException {
        Thread.sleep(6000);
        onView(withId(R.id.profilePageBtn)).perform(click());
        getClass().equals(PlaylistGenerator.class);
    }
    @Test
    public void clickPayment() throws InterruptedException {
        Thread.sleep(6000);
        onView(withId(R.id.paymentPageBtn)).perform(click());
        getClass().equals(PaymentStartActivity.class);
    }


}

