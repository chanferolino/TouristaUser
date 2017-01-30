package com.touristadev.tourista.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import com.touristadev.tourista.fragments.BookedToursFragment;
import com.touristadev.tourista.fragments.CustomPackageFragment;
import com.touristadev.tourista.fragments.ForYouFragment;
import com.touristadev.tourista.fragments.WishListFragment;

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

public class TourActivity extends AppCompatActivity {
    private static final String[] CHANNELS = new String[]{"BOOKED TOURS","REQUESTED TOURS" , "MY TOURS"};
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();
    BottomBar mBottomBar;
    public ForYouFragment t= new ForYouFragment();
    public  FragmentManager fragmentManager;
    private String firstName,lastName, email;

    private Typeface myCustomFont;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
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
        setContentView(R.layout.activity_tour);

        initFragments();
        initMagicIndicator1();

        mFragmentContainerHelper.handlePageSelected(1, false);
        switchPages(1);

        Intent i = getIntent();
        myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Bold.ttf");
        firstName = i.getStringExtra("firstName");
        lastName = i.getStringExtra("lastName");
        email = i.getStringExtra("email");

        mBottomBar= BottomBar.attach(this,savedInstanceState);
        mBottomBar.useFixedMode();
        mBottomBar.setTypeFace("fonts/Poppins-Regular.ttf");
        mBottomBar.setDefaultTabPosition(2);
        mBottomBar.setActiveTabColor(Color.parseColor("#fecd23"));
        mBottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

                if(menuItemId== R.id.bottombar1)
                {

                    Intent i = new Intent(TourActivity.this, ExploreActivity.class);
                    startActivity(i);
                }
                if(menuItemId== R.id.bottombar2)
                {
                    Intent i = new Intent(TourActivity.this, DiscoverActivity.class);
                    startActivity(i);
                }
//                if(menuItemId== R.id.bottombar3)
//                {
//
//                }
                if(menuItemId== R.id.bottombar4)
                {
                    Intent intent = new Intent(TourActivity.this, PassportActivity.class);
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
    public boolean onCreateOptionsMenu (Menu menu){

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
       BookedToursFragment booked = new BookedToursFragment();
        WishListFragment wish = new WishListFragment();
        CustomPackageFragment cus = new CustomPackageFragment();

        mFragments.add(booked);
        mFragments.add(wish);
        mFragments.add(cus);


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
                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#fdd8a1"));
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
