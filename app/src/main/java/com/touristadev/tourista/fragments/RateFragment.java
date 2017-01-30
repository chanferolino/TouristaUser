package com.touristadev.tourista.fragments;

/**
 * Created by Christian on 1/2/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.touristadev.tourista.activities.CustomPackageActivity;
import com.touristadev.tourista.activities.ShadowTransformer;
import com.touristadev.tourista.adapters.CardFragmentPagerAdapter;
import com.touristadev.tourista.adapters.RateAdapter;
import com.touristadev.tourista.adapters.TGAdapter;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.BookedPackages;
import com.touristadev.tourista.dataModels.RatingCard;
import com.touristadev.tourista.dataModels.TouristaPackages;
import com.touristadev.tourista.models.ExploreCard;
import com.touristadev.tourista.models.TourGuideModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Christian on 12/1/2016.
 */

public class RateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Controllers con = new Controllers();
    // TODO: Rename and change types of parameters
    private BookedPackages currPack = new BookedPackages();

    private OnFragmentInteractionListener mListener;
    private ArrayList<TouristaPackages> PackageList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mCardAdapter;
    private ArrayList<TourGuideModel> tourGuideList = new ArrayList<>();
    private ArrayList<RatingCard> cardList = new ArrayList<>();
    private static String packagenameTG;

    public RateFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RateFragment newInstance(String pack) {
        RateFragment fragment = new RateFragment();
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
            Controllers fincon = new Controllers();
            PackageList = fincon.getControllerPackaaes();
            for(int x = 0 ; x<PackageList.size();x++){
                if(PackageList.get(x).getPackageName().equals(packagenameTG)){
                    cardList.add(new RatingCard(PackageList.get(x).getPackageName(),0,PackageList.get(x).getPackageImage()));


                }
            }
            con.setCurrTourGuide(tourGuideList);
            tourGuideList = con.getCurrentTourguideList();
            for(int x = 0; x<tourGuideList.size();x++) {

                cardList.add(new RatingCard(tourGuideList.get(x).getTgName(), 0, tourGuideList.get(x).getTgImage()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rating, container, false);




        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_recycler_rate);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);


        mCardAdapter = new RateAdapter(cardList,packagenameTG);
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
