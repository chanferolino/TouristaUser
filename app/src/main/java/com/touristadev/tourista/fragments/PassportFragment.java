package com.touristadev.tourista.fragments;

/**
 * Created by Christian on 11/28/2016.
 */


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.firebase.auth.FirebaseUser;
import com.touristadev.tourista.R;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.models.CurrentUser;

import de.hdodenhof.circleimageview.CircleImageView;


public class PassportFragment extends Fragment {
    MaterialViewPager materialViewPager;
    private CircleImageView imageView;
    private FirebaseUser user;
    private static AppCompatActivity ac;
    private Controllers mControllers = new Controllers();
    public static PassportFragment newInstance(AppCompatActivity cone) {
        ac = cone;
        return new PassportFragment();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        user = mControllers.getUser();


        final int tabCount = 1;
        this.materialViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndDrawable(R.color.colorHeader1, getResources().getDrawable(R.mipmap.stafe));
                }
                return null;
            }
        });
        this.materialViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return PassportDetailsFragment.newInstance(ac);
                }
                return PassportDetailsFragment.newInstance(ac);
            }

            @Override
            public int getCount() {
                return tabCount;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return CurrentUser.name;
                    default:
                        return "Page " + position;
                }
            }
        });
        this.materialViewPager.getViewPager().setOffscreenPageLimit(tabCount);
        this.materialViewPager.getPagerTitleStrip().setViewPager(this.materialViewPager.getViewPager());
        Glide.with(this.getActivity()).load(CurrentUser.photoUrl).into(imageView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        this.materialViewPager = (MaterialViewPager) view.findViewById(R.id.viewPager);
        imageView = (CircleImageView) view.findViewById(R.id.profile_image);





        return view;
    }


}
