package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import de.robinkuck.nodechat.android.R;
import de.robinkuck.nodechat.android.fragments.ChatlistFragment;
import de.robinkuck.nodechat.android.fragments.UserlistFragment;
import de.robinkuck.nodechat.android.managers.CustomActivityManager;
import de.robinkuck.nodechat.android.managers.NickManager;
import de.robinkuck.nodechat.android.managers.SocketManager;

public class MainActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        configTabs();
        super.configViews();
        super.configKeyboard((ViewGroup) findViewById(R.id.rootLayout));

        if (NickManager.getInstance().getCurrentNick().equals("")) {
            CustomActivityManager.getInstance().startNickActivity(this);
        }
        /*
        //TODO check this in splashActivity
        if (SocketManager.getInstance().getStatus() != SocketManager.Status.CONNECTED) {
            CustomActivityManager.getInstance().startNickActivity(this);
        }
        */
        /*
        System.out.println("DEVICEID: " + Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID));
                */
    }

    @Override
    public void onResume() {
        super.onResume();
        CustomActivityManager.getInstance().setCurrentActivity(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void onSettings(final View view) {
        CustomActivityManager.getInstance().startSettingsActivity(this);
    }

    public void setChatlistFragment() {
        ChatlistFragment fragment = new ChatlistFragment();
        fragment.setArguments(getIntent().getExtras());

        FragmentTransaction transac = getSupportFragmentManager().beginTransaction();
        transac.replace(R.id.container, fragment);
        transac.addToBackStack("chatlist");
        transac.commit();
    }

    private void configTabs() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        ViewPager pager = (ViewPager) findViewById(R.id.tabpager);
        PagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ChatlistFragment();
                    case 1:
                        return new UserlistFragment();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Chats";
                    case 1:
                        return "Users";
                    default:
                        return null;
                }
            }
        };
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                MainActivity.super.softKeyboard.closeSoftKeyboard();
            }
        });
    }

}
