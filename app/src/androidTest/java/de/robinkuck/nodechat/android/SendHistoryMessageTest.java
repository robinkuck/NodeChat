package de.robinkuck.nodechat.android;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.robinkuck.nodechat.android.activities.NickActivity;
import de.robinkuck.nodechat.android.activities.SplashActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;

@RunWith(AndroidJUnit4.class)
public class SendHistoryMessageTest {

    @Rule
    public ActivityTestRule<NickActivity> nickActivityActivityTestRule = new ActivityTestRule<>(NickActivity.class);
    @Rule
    public RepeatRule repeatRule = new RepeatRule();

    @Test
    //@RepeatRule.Repeat(times = 100)
    public void sendGlobalMessageTest() {
        final String message = "Hello there!";
        simpleLogin("Bill");
        onView(ViewMatchers.withId(de.robinkuck.nodechat.android.R.id.chatentry_global)).perform(click());
        onView(ViewMatchers.withId(de.robinkuck.nodechat.android.R.id.editMessage)).perform(typeText(message), closeSoftKeyboard());
        onView(ViewMatchers.withId(de.robinkuck.nodechat.android.R.id.sendButton)).perform(click());
    }

    @Test
    //@RepeatRule.Repeat(times = 100)
    public void sendGlobalMessageTest2() {
        final String message = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore";
        simpleLogin("Bill");
        onView(ViewMatchers.withId(de.robinkuck.nodechat.android.R.id.chatentry_global)).perform(click());
        onView(ViewMatchers.withId(de.robinkuck.nodechat.android.R.id.editMessage)).perform(typeText(message), closeSoftKeyboard());
        onView(ViewMatchers.withId(de.robinkuck.nodechat.android.R.id.sendButton)).perform(click());
    }

    private void simpleLogin(final String nickName) {
        onView(ViewMatchers.withId(de.robinkuck.nodechat.android.R.id.editNick)).perform(clearText(), typeText(nickName));
        onView(ViewMatchers.withId(de.robinkuck.nodechat.android.R.id.buttonEnter)).perform(click());
        wait(1000);
    }

    private void wait(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
