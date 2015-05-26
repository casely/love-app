package com.example.savqa.love;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private ViewPagerAdapter mAdapter;


    // Tab titles

    final int[] ICONS = new int[] {
            R.drawable.ic_msg_fill,
            R.drawable.ic_heart_fill,
            R.drawable.ic_user_fill,
    };
    //private String[] tabs = { "Сообщения", "Знакомства", "Профиль" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);

        final ActionBar actionBar = getSupportActionBar();
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (int i = 0; i < 3; i++) {
            actionBar.addTab(actionBar.newTab().setIcon(MainActivity.this.getResources().getDrawable(ICONS[i]))
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        viewPager.setCurrentItem(1, false);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
        final ActionBar actionBar = getSupportActionBar();
        switch(tab.getPosition()) {
            case 0:
                actionBar.setTitle("Сообщения");
                break;
            case 1:
                actionBar.setTitle("Знакомства");
                break;
            case 2:
                actionBar.setTitle("Профиль");
                break;
            default:
                actionBar.setTitle("Love.ykt");
                break;
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_filter) {
            Intent intent = new Intent(this, FilterActivity.class);
            startActivity(intent);

            return true;
        }else if (id == R.id.menu_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {

            if (viewPager.getCurrentItem() == 0) {
                menu.findItem(R.id.menu_filter).setVisible(false);
                menu.findItem(R.id.menu_settings).setVisible(false);
                return true;
            } else if (viewPager.getCurrentItem() == 1) {
                menu.findItem(R.id.menu_filter).setVisible(true);
                menu.findItem(R.id.menu_settings).setVisible(false);
                return true;
            } else if (viewPager.getCurrentItem() == 2) {
                menu.findItem(R.id.menu_filter).setVisible(false);
                menu.findItem(R.id.menu_settings).setVisible(true);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onPrepareOptionsMenu(menu);
    }

}
