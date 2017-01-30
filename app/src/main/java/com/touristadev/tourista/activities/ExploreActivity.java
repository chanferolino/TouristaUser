package com.touristadev.tourista.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.touristadev.tourista.R;
import com.touristadev.tourista.fragments.RecentFriendsActFragment;
import com.touristadev.tourista.fragments.ForYouFragment;
import com.touristadev.tourista.fragments.HotSpotsFragment;
import com.touristadev.tourista.fragments.HotToursFragment;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class ExploreActivity extends AppCompatActivity {
    private static final String[] CHANNELS = new String[]{"RECOMMENDED", "TOURS", "SPOTS", "FRIENDS ACTIVITY"};
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();
    BottomBar mBottomBar;
    public ForYouFragment t = new ForYouFragment();
    public FragmentManager fragmentManager;
    private String firstName, lastName, email;
    private Typeface myCustomFont;



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_explore);


        initFragments();
        initMagicIndicator1();

        mFragmentContainerHelper.handlePageSelected(0, false);
        switchPages(0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Create Package", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(ExploreActivity.this, CustomPackageActivity.class);
                i.putExtra("type","create");

                startActivity(i);
            }
        });

        Intent i = getIntent();

        firstName = i.getStringExtra("firstName");
        lastName = i.getStringExtra("lastName");
        email = i.getStringExtra("email");

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.useFixedMode();
        mBottomBar.setDefaultTabPosition(0);
        mBottomBar.setTypeFace("fonts/Poppins-Regular.ttf");
        mBottomBar.setActiveTabColor(Color.parseColor("#fecd23"));
        mBottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                if (menuItemId == R.id.bottombar1) {
                    t = new ForYouFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, t).commit();
                }
                if (menuItemId == R.id.bottombar2) {
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    Intent i = new Intent(ExploreActivity.this, DiscoverActivity.class);
                    startActivity(i);
                }
                if (menuItemId == R.id.bottombar3) {

                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    Intent i = new Intent(ExploreActivity.this, TourActivity.class);
                    startActivity(i);
                }
                if (menuItemId == R.id.bottombar4) {
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    Intent intent = new Intent(ExploreActivity.this, PassportActivity.class);
                    intent.putExtra("firstName", firstName);
                    intent.putExtra("lastName", lastName);
                    intent.putExtra("email", email);
                    intent.putExtra("tourguidemode",false);
                    startActivity(intent);
                }



            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void switchPages(int index) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        for (int i = 0, j = mFragments.size(); i < j; i++) {
            if (i == index) {
                continue;
            }

            fragment = mFragments.get(i);

            if (fragment.isAdded()) {
                fragmentTransaction.hide(fragment);
            }
        }
        fragment = mFragments.get(index);
        if (fragment.isAdded()) {

            fragmentTransaction.show(fragment);
        } else {

            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void initFragments() {
        ForYouFragment ForyouFrag = new ForYouFragment();
        HotSpotsFragment HotspotFrag = new HotSpotsFragment();
        HotToursFragment HotTourFrag = new HotToursFragment();
        RecentFriendsActFragment promosfrag = new RecentFriendsActFragment();
        mFragments.add(ForyouFrag);
        mFragments.add(HotTourFrag);
        mFragments.add(HotspotFrag);
        mFragments.add(promosfrag);

    }

    private void initMagicIndicator1() {



        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);

        CommonNavigator commonNavigator = new CommonNavigator(this);

        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return CHANNELS.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Bold.ttf");

                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#BBDEFB"));
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#FFFFFF"));
                colorTransitionPagerTitleView.setText(CHANNELS[index]);
                colorTransitionPagerTitleView.setGravity(Gravity.CENTER);
                colorTransitionPagerTitleView.setTypeface(myCustomFont);


                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        mFragmentContainerHelper.handlePageSelected(index);
                        switchPages(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor("#FFFFFF"));
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator);
    }
}
