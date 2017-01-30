package com.touristadev.tourista.fragments;

/**
 * Created by Christian on 11/28/2016.
 */


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.touristadev.tourista.R;
import com.touristadev.tourista.adapters.PassportDetailsAdapter;

import java.util.List;


public class PassportDetailsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapterM;
    public static AppCompatActivity context;
    private String ratingsCount,tripsCount,badgesCount;
    List<String> Squad;
    List<Integer>  Trips;
    public static PassportDetailsFragment newInstance(AppCompatActivity cone) {
        context = cone;

        return new PassportDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profiledetails, container, false);



        return view;

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        ratingsCount = "2";
        tripsCount = "1";
        badgesCount = "1";

        mAdapterM = new RecyclerViewMaterialAdapter(new PassportDetailsAdapter(ratingsCount,tripsCount,badgesCount,Squad,Trips,context));
        mRecyclerView.setAdapter(mAdapterM);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);



    }


}