package com.example.sumon.androidvolley;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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
public class SystemTestPayment {

    @Rule
    public ActivityTestRule<PaymentEndActivity> activityRule = new
            ActivityTestRule<>(PaymentEndActivity.class);

    @Test
    public void clickConfirm() throws InterruptedException {
        onView(withId(R.id.cardNumberText)).perform(typeText("1"));
        onView(withId(R.id.cardSecurityCodeText)).perform(typeText("1"));
        onView(withId(R.id.cardNameText)).perform(typeText("1"));
        onView(withId(R.id.cardZipCodeText)).perform(typeText("1"));
        onView(withId(R.id.expirationDateText)).perform(typeText("1"), closeSoftKeyboard());

        onView(withId(R.id.paymentConfirmBtn)).perform(click());
        onView(withId(R.id.cardInfoText)).check(matches(withText("Thank you for becoming a paid member. It's people like you that" +
        " allow us to keep doing what we love!")));
        Thread.sleep(6000);
        onView(withId(R.id.returnToDashFromPayment)).perform(click());
        GlobalUser.setMembershipType("Unpaid User");
    }

}
