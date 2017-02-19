package com.touristadev.tourista.adapters;

/**
 * Created by Christian on 12/27/2016.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.touristadev.tourista.R;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.TouristaPackages;
import com.touristadev.tourista.models.TourGuideModel;

import java.util.ArrayList;
import java.util.List;

public class TourGuideAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<TourGuideModel> mData;
    private ArrayList<TouristaPackages> packlist = new ArrayList<>();

    private ArrayList<String> spotList = new ArrayList<>();
    private ImageView imgTG;
    private TextView SpotName,TGName,TGAge,TGMotto;
    private RatingBar ratBar;
    private float mBaseElevation;
    private String Packname;
    LayoutInflater mInflater;
    private Context context;
    public  TourGuideAdapter(ArrayList<TourGuideModel> Data,String PackName) {

        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        Packname = PackName;

        if(Data!=null){
            for (int i = 0; i < Data.size(); i++) {
                mData.add(Data.get(i));

                mViews.add(null);

            }
        }
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.card_item_tourguide, container, false);
        packlist = Controllers.getControllerPackaaes();
        for(int x = 0 ; x<packlist.size();x++){
            if(packlist.get(x).getPackageName().equals(Packname)){
               spotList.add(packlist.get(x).getSpotItinerary().get(x).getSpotName());
            }
        }
       mInflater = LayoutInflater.from(container.getContext());
        imgTG = (ImageView) view.findViewById(R.id.imgCard);
        SpotName = (TextView) view.findViewById(R.id.txtSpotName);
        TGName = (TextView) view.findViewById(R.id.txtSpotName);
        TGAge = (TextView) view.findViewById(R.id.txtSpotName);
        TGMotto = (TextView) view.findViewById(R.id.txtSpotName);
        ratBar = (RatingBar) view.findViewById(R.id.rtTGBar);

        imgTG.setImageResource(mData.get(position).getTgImage());
        SpotName.setText(spotList.get(position));
        TGName.setText(mData.get(position).getTgName());
        TGAge.setText(mData.get(position).getTgAge());
        TGMotto.setText(mData.get(position).getTgMotto());
        ratBar.setRating(mData.get(position).getTgStars());
        context = view.getContext();


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

}