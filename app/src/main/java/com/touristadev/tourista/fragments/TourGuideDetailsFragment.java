package com.touristadev.tourista.fragments;

/**
 * Created by Christian on 12/27/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touristadev.tourista.R;
import com.touristadev.tourista.activities.ShadowTransformer;
import com.touristadev.tourista.adapters.CardFragmentPagerAdapter;
import com.touristadev.tourista.adapters.TGAdapter;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.models.ExploreCard;
import com.touristadev.tourista.models.TourGuideModel;

import java.util.ArrayList;

/**
 * Created by Christian on 12/1/2016.
 */

public class TourGuideDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    // TODO: Rename and change types of parameters
    private OnFragmentInteractionListener mListener;

    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mCardAdapter;
    private ArrayList<TourGuideModel> tourGuideList = new ArrayList<>();
    private static String packagenameTG;

    public TourGuideDetailsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TourGuideDetailsFragment newInstance(String pack) {
        TourGuideDetailsFragment fragment = new TourGuideDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, pack);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            packagenameTG = getArguments().getString("ARG_PARAM1");

            tourGuideList = Controllers.getTourguideList();

            Controllers.setCurrTourGuide(tourGuideList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bookedtours, container, false);




        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_recycler_bookedtours);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        Log.d("packTG","TGsize: "+tourGuideList.size());

        Log.d("packTG","TGsize: "+packagenameTG);
        mCardAdapter = new TGAdapter(tourGuideList,packagenameTG);
        mRecyclerView.setAdapter(mCardAdapter);
        mCardAdapter.notifyDataSetChanged();




        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
 }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
