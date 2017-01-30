package com.touristadev.tourista.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class TGAdapter extends RecyclerView.Adapter<TGAdapter.MyViewHolder>{

    private List<CardView> mViews;
    private List<TourGuideModel> mData;
    private ArrayList<TouristaPackages> packlist = new ArrayList<>();

    private ArrayList<String> spotList = new ArrayList<>();

    private String Packname;
    private Context context;
    private Controllers con = new Controllers();
    public  TGAdapter(ArrayList<TourGuideModel> Data,String PackName) {

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


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgTG;
        private TextView SpotName,TGName,TGAge,TGMotto;
        private RatingBar ratBar;
        public MyViewHolder(View v) {
            super(v);
            View view = v;
            imgTG = (ImageView) view.findViewById(R.id.imgCard);
            SpotName = (TextView) view.findViewById(R.id.txtSpotName);
            TGName = (TextView) view.findViewById(R.id.txtTGName);
            TGAge = (TextView) view.findViewById(R.id.txtAge);
            TGMotto = (TextView) view.findViewById(R.id.txtMotto);
            ratBar = (RatingBar) view.findViewById(R.id.rtTGBar);
            ratBar.setFocusable(false);
            ratBar.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        int size = 0;
        size = mData.size();
        return size;
    }

    @Override
    public TGAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_tourguide, parent, false);
        context = view.getContext();

        packlist = con.getControllerPackaaes();
        for(int x = 0 ; x<packlist.size();x++){
            if(packlist.get(x).getPackageName().equals(Packname)){
                for(int y= 0 ; y<packlist.get(x).getSpotItinerary().size();y++) {
                    spotList.add(packlist.get(x).getSpotItinerary().get(y).getSpotName());
                    Log.d("Chan12/28/16D", spotList.get(y) + "");
                }
            }
        }

        return new TGAdapter.MyViewHolder(view) {
        };
    }


    @Override
    public void onBindViewHolder(TGAdapter.MyViewHolder holder, int position) {
        holder.imgTG.setImageResource(mData.get(position).getTgImage());
        holder.SpotName.setText(spotList.get(position));
        holder.TGName.setText(mData.get(position).getTgName());
        holder.TGAge.setText("Age"+mData.get(position).getTgAge());
        holder.TGMotto.setText("Personal Description\n"+mData.get(position).getTgMotto());
        holder.ratBar.setRating(mData.get(position).getTgStars());


    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }




}