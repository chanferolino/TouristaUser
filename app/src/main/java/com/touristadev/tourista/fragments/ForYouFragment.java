package com.touristadev.tourista.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.touristadev.tourista.R;
import com.touristadev.tourista.activities.ShadowTransformer;
import com.touristadev.tourista.adapters.CardFragmentPagerAdapter;
import com.touristadev.tourista.adapters.CardPagerAdapter;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.TouristaPackages;
import com.touristadev.tourista.dataModels.Spots;
import com.touristadev.tourista.models.ExploreCard;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForYouFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForYouFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForYouFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView btnTourSA, btnSpotSA, btnDealSA;
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();

    // TODO: Rename and change types of parameters
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private String mParam1;
    private String mParam2;
    private ViewPager mViewPagerTours, mViewPagerSpots, mViewPagerDeals;
    private ArrayList<Bitmap> mTourImg, mSpotImg, mDealImg;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private Fragment fragment;
    private ArrayList<TouristaPackages> TourListTemp = new ArrayList<>();
    private ArrayList<TouristaPackages> recentActivityList = new ArrayList<>();
    private ArrayList<Spots> SpotListTemp = new ArrayList<>();
    private ArrayList<TouristaPackages> DealListTemp = new ArrayList<>();
    private ArrayList<ExploreCard> TourList = new ArrayList<>();
    private ArrayList<ExploreCard> SpotList = new ArrayList<>();
    private ArrayList<ExploreCard> DealList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private boolean mShowingFragments = false;
    private OnFragmentInteractionListener mListener;

    //Fonts
    private TextView txtTour, txtSpot, txtDeals;

    public ForYouFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForYouFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForYouFragment newInstance(String param1, String param2) {
        ForYouFragment fragment = new ForYouFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_for_you, container, false);

        //FONTS
        Typeface fontPoppinsRegular = Typeface.createFromAsset(getContext().getAssets(), "fonts/Poppins-Regular.ttf");

        txtTour = (TextView) view.findViewById(R.id.txtTour);
        txtTour.setTypeface(fontPoppinsRegular);

        txtSpot = (TextView) view.findViewById(R.id.txtSpot);
        txtSpot.setTypeface(fontPoppinsRegular);

        txtDeals = (TextView) view.findViewById(R.id.txtDeals);
        txtDeals.setTypeface(fontPoppinsRegular);

        mViewPagerTours = (ViewPager) view.findViewById(R.id.viewPagerTours);
        btnTourSA = (TextView) view.findViewById(R.id.btnTourSeeAll);
        btnSpotSA = (TextView) view.findViewById(R.id.btnSpotSeeAll);
        btnDealSA = (TextView) view.findViewById(R.id.btnDealsSeeAll);

        btnTourSA = (TextView) view.findViewById(R.id.btnTourSeeAll);
        btnTourSA.setTypeface(fontPoppinsRegular);

        btnSpotSA = (TextView) view.findViewById(R.id.btnSpotSeeAll);
        btnSpotSA.setTypeface(fontPoppinsRegular);

        btnDealSA = (TextView) view.findViewById(R.id.btnDealsSeeAll);
        btnDealSA.setTypeface(fontPoppinsRegular);




        initFragments();
        SpotListTemp.clear();
        TourListTemp.clear();
        DealListTemp.clear();
        SpotListTemp = Controllers.getControllerSpots();
        if(SpotListTemp.size()!=0){
            TourListTemp = Controllers.getControllerPackaaes();
            DealListTemp = Controllers.getControllerPackaaes();
        }
        btnTourSA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFragmentContainerHelper.handlePageSelected(1);
                switchPages(1);
            }
        });
        btnSpotSA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFragmentContainerHelper.handlePageSelected(2);
                switchPages(2);
            }
        });
        btnDealSA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFragmentContainerHelper.handlePageSelected(3);
                switchPages(3);

            }
        });
        TourList.clear();
        if (TourListTemp != null) {
            for (int x = 0; x < TourListTemp.size(); x++) {
                TourList.add(new ExploreCard(TourListTemp.get(x).getPackageName(), TourListTemp.get(x).getRating(), "₱ " + String.valueOf(TourListTemp.get(x).getPackPrice()), String.valueOf(TourListTemp.get(x).getPackageNoOfSpots()) + " Spots", String.valueOf(TourListTemp.get(x).getPackageTotalNoOfHours()) + " Hours", "tour",TourListTemp.get(x).getPackageImage(),TourListTemp.get(x).getCompanyName()));

            }
        }

//
// image list tour and deals
        mCardAdapter = new CardPagerAdapter(TourList);
        FragmentManager fm = getFragmentManager();
        mFragmentCardAdapter = new CardFragmentPagerAdapter(fm,
              0);

        mCardAdapter.notifyDataSetChanged();
        mViewPagerTours.setAdapter(mCardAdapter);

        mViewPagerTours.setOffscreenPageLimit(3);
//------------------------------------------------------------------------------------------------
        SpotList.clear();
        if (SpotListTemp != null) {
            for (int x = 0; x < SpotListTemp.size(); x++) {
                SpotList.add(new ExploreCard(SpotListTemp.get(x).getSpotName(), SpotListTemp.get(x).getSpotRating(), " ", " ", " ", "spot",SpotListTemp.get(x).getSpotImage(),""));

            }
        }

        mViewPagerSpots = (ViewPager) view.findViewById(R.id.viewPagerSpot);
        mCardAdapter = new CardPagerAdapter(SpotList);
        FragmentManager fm2 = getFragmentManager();
        mFragmentCardAdapter = new CardFragmentPagerAdapter(fm2,
                dpToPixels(0, getContext()));

        mCardAdapter.notifyDataSetChanged();
        mViewPagerSpots.setAdapter(mCardAdapter);
        mViewPagerSpots.setOffscreenPageLimit(3);
//------------------------------------------------------------------------------------------------
        recentActivityList.clear();
        DealList.clear();
        recentActivityList = Controllers.getRecentList();

        Log.d("changwapo","Recent "+recentActivityList.size());
        if (recentActivityList != null) {
            for (int x = 0; x < recentActivityList.size(); x++) {
                DealList.add(new ExploreCard(recentActivityList.get(x).getPackageName(), recentActivityList.get(x).getRating(), "₱ " + String.valueOf(recentActivityList.get(x).getPackPrice()), String.valueOf(recentActivityList.get(x).getPackageNoOfSpots()) + " Spots", String.valueOf(recentActivityList.get(x).getPackageTotalNoOfHours()) + " Hours", "tour",recentActivityList.get(x).getPackageImage(),recentActivityList.get(x).getCompanyName()));

            }
        }


        mViewPagerDeals = (ViewPager) view.findViewById(R.id.viewPagerDeals);
        mCardAdapter = new CardPagerAdapter(DealList);
        FragmentManager fm3 = getFragmentManager();
        mFragmentCardAdapter = new CardFragmentPagerAdapter(fm3,
                dpToPixels(0, getContext()));

        mCardAdapter.notifyDataSetChanged();
        mViewPagerDeals.setAdapter(mCardAdapter);
        mViewPagerDeals.setOffscreenPageLimit(3);
        return view;
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }

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

    public void switchPages(int index) {
        fragmentManager = getFragmentManager();
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
}
