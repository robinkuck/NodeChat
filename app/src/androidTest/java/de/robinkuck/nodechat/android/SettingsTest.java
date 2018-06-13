package de.robinkuck.nodechat.android;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.robinkuck.nodechat.android.activities.NickActivity;
import de.robinkuck.nodechat.android.managers.ChatHistoryManager;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SettingsTest {

    @Rule
    public ActivityTestRule<NickActivity> nickActivityActivityTestRule = new ActivityTestRule<>(NickActivity.class);

    @Test
    public void muteGlobalChatTest() {
        TestUtils.simpleLogin("testuser02");
        onView(ViewMatchers.withId(de.robinkuck.nodechat.android.R.id.chatentry_global)).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Mute")).perform(click());
        Assert.assertTrue(ChatHistoryManager.getInstance().getGlobalChatHistory().isMuted());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Unmute")).perform(click());
        Assert.assertFalse(ChatHistoryManager.getInstance().getGlobalChatHistory().isMuted());
    }

    @Test
    public void clearGlobalChatHistoryTest() {
        TestUtils.simpleLogin("testuser02");
        onView(ViewMatchers.withId(de.robinkuck.nodechat.android.R.id.chatentry_global)).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Clear history")).perform(click());
        onView(withText("Yes")).perform(click());
        Assert.assertEquals(0, ChatHistoryManager.getInstance().getGlobalChatHistory().getMessages().size());
    }

}
