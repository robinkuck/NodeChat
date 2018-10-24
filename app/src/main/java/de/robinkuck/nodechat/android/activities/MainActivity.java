package de.robinkuck.nodechat.android.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import de.robinkuck.nodechat.android.R;
import de.robinkuck.nodechat.android.fragments.ChatlistFragment;
import de.robinkuck.nodechat.android.fragments.UserlistFragment;
import de.robinkuck.nodechat.android.managers.CustomActivityManager;
import de.robinkuck.nodechat.android.managers.NickManager;

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
        } else {
            finish();
        }
    }

    public void onOpenSettings(final View view) {
        CustomActivityManager.getInstance().startSettingsActivity(this);
    }

    private void configTabs() {
        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager pager = findViewById(R.id.tabpager);
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
