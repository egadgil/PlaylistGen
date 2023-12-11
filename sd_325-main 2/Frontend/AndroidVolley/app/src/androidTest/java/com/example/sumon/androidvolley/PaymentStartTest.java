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
public class PaymentStartTest {

    @Rule
    public ActivityTestRule<PaymentStartActivity> activityRule = new
            ActivityTestRule<>(PaymentStartActivity.class);

    @Test
    public void clickYes() throws InterruptedException {

        GlobalUser.setMembershipType("Unpaid Member");
        onView(withId(R.id.paymentYesBtn)).perform(click());
        getClass().equals(PaymentEndActivity.class);
    }

}
