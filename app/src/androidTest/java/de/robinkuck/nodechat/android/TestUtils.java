package de.robinkuck.nodechat.android;

import android.support.test.espresso.matcher.ViewMatchers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;

public class TestUtils {


    public static void simpleLogin(final String nickName) {
        onView(ViewMatchers.withId(de.robinkuck.nodechat.android.R.id.editNick)).perform(clearText(), typeText(nickName));
        onView(ViewMatchers.withId(de.robinkuck.nodechat.android.R.id.buttonEnter)).perform(click());
        wait(1000);
    }

    public static void wait(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
