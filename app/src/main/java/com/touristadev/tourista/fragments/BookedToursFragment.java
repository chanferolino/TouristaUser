package com.touristadev.tourista.fragments;

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

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import com.google.common.io.BaseEncoding;
import com.touristadev.tourista.R;
import com.touristadev.tourista.activities.ShadowTransformer;
import com.touristadev.tourista.adapters.CardExplorerPagerAdapter;
import com.touristadev.tourista.adapters.CardFragmentPagerAdapter;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.BookedPackages;
import com.touristadev.tourista.dataModels.TouristaPackages;
import com.touristadev.tourista.models.CurrentUser;
import com.touristadev.tourista.models.ExploreCard;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Christian on 12/1/2016.
 */

public class BookedToursFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public  List<String> eventStrings = new ArrayList<String>();
    Calendar mService;
    GoogleAccountCredential mFinalCredential;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<ExploreCard> BookedList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mCardAdapter;

    private ArrayList<BookedPackages> BookedListTemp = new ArrayList<>();
    public BookedToursFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HotToursFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HotToursFragment newInstance(String param1, String param2) {
        HotToursFragment fragment = new HotToursFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bookedtours, container, false);
        BookedListTemp.clear();
        BookedList.clear();
        BookedListTemp = Controllers.getBookedList();
        mFinalCredential = Controllers.getCredentials();
        MakeRequestTask mak = new MakeRequestTask(mFinalCredential);
        mak.execute();
        if (BookedListTemp != null) {
            for (int x = 0; x < BookedListTemp.size(); x++) {
                BookedList.add(new ExploreCard(BookedListTemp.get(x).getPackageName(), BookedListTemp.get(x).getRating(), "₱ " + String.valueOf(BookedListTemp.get(x).getPackageTotalNoOfHours()*40), String.valueOf(BookedListTemp.get(x).getPackageNoOfSpots()) + " Spots", "Start Date: "+String.valueOf(BookedListTemp.get(x).getPackageTotalNoOfHours()) , "tour",BookedListTemp.get(x).getPackageImage(),BookedListTemp.get(x).getCompanyName()));

            }
        }
        else{
            Log.d("BookedToursFragment","No booked");
        }
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_recycler_bookedtours);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mCardAdapter = new CardExplorerPagerAdapter(BookedList,"Bookedlist");
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
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            Calendar service = new Calendar.Builder(transport, jsonFactory, credential)
                    .setApplicationName("Tourista").build();
            mService = service;


        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {

                return getDataFromApi();

            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<String> getDataFromApi() throws IOException {
            String pageToken = null;
            do {
                Events events = mService.events().list("primary").setPageToken(pageToken).execute();
                List<Event> items = events.getItems();
                for (int x = 0 ; x <items.size();x++) {
                    eventStrings.add(items.get(x).getId());
                }
                pageToken = events.getNextPageToken();
            } while (pageToken != null);
            for(int x = 0 ; x < eventStrings.size();x++) {
                for(int z = 0 ; z < BookedListTemp.size();z++) {
                    String id = CurrentUser.userFirebaseId+""+BookedListTemp.get(z).getDate();

                    if (!Objects.equals(eventStrings.get(x), id)){
                        mService.events().delete("primary", ""+eventStrings.get(x)).execute();
                    }
                }
            }
            return eventStrings;
        }


    }
}
