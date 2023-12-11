package com.example.sumon.androidvolley;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.action.TypeTextAction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignupActivityTest {



    @Rule
    public ActivityTestRule<SignupActivity> activityRule = new
            ActivityTestRule<>(SignupActivity.class);

    @Test
    public void firstButtonPress() {
        onView(withId(R.id.enterBtn)).perform(click());
    }
    @Test
    public void secondButtonPress() {
        onView(withId(R.id.enterBtn)).perform(click());
        onView(withId(R.id.securityCodeText)).perform(typeText("440"));
        onView(withId(R.id.enterBtnLast)).perform(click());
    }

    /*@Test
    public void fullSignupSysTest() throws InterruptedException {
        onView(withId(R.id.signUpUsername)).perform(typeText("uniquechar111"));
        onView(withId(R.id.signupPassword)).perform(typeText("1"));
        onView(withId(R.id.signupFullName)).perform(typeText("1"));
        onView(withId(R.id.signupEmail)).perform(typeText("11@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.enterBtn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.securityCodeText)).perform(typeText("440"), closeSoftKeyboard());
        onView(withId(R.id.enterBtnLast)).perform(click());
        Thread.sleep(6000);
        onView(withId(R.id.userDashboardTxt)).check(matches(withText("" +
                "Welcome uniquechar11 to our user dashboard\n Take a look around," +
                "generate some playlists!")));

    }*/
    //MAYBE MAKE A RANDOM USER
}
