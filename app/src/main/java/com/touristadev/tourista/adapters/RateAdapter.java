package com.touristadev.tourista.adapters;

/**
 * Created by Christian on 1/2/2017.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.touristadev.tourista.R;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.RatingCard;
import com.touristadev.tourista.dataModels.RatingTG;

import java.util.ArrayList;
import java.util.List;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.MyViewHolder>{

    private List<CardView> mViews;
    private List<RatingCard> mData;
    private String Packname;
    public  RateAdapter(ArrayList<RatingCard> Data, String PackName) {

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
        private TextView TGName;
        private RatingBar rtKnowledge,rtPersonality,rtProfessional,rtPackage;
        private TextView txtKnowledge,txtPersonality,txtProfessional;
        private float knowledge,personality,professional;
        private int flag=0;
        public MyViewHolder(View v) {
            super(v);
            View view = v;
            imgTG = (ImageView) view.findViewById(R.id.imgCard);
            TGName = (TextView) view.findViewById(R.id.txtTGName);
            rtProfessional = (RatingBar) view.findViewById(R.id.rtTGBar);
            rtKnowledge = (RatingBar) view.findViewById(R.id.rtKnowledge);
            rtPersonality = (RatingBar) view.findViewById(R.id.rtPersonality);
            rtPackage = (RatingBar) view.findViewById(R.id.rtPackage);
            txtKnowledge = (TextView) view.findViewById(R.id.txtKnow);
            txtPersonality = (TextView) view.findViewById(R.id.txtPersonality);
            txtProfessional = (TextView) view.findViewById(R.id.txtPro);
            rtProfessional.setFocusable(false);
            flag = 0;
            rtProfessional.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                professional =  ratingBar.getRating();
                    flag+=1;
                }
            });
            rtKnowledge.setFocusable(false);
            rtKnowledge.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    knowledge =  ratingBar.getRating();
                    flag+=1;
                }
            });
            rtPersonality.setFocusable(false);
            rtPersonality.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    personality =  ratingBar.getRating();
                    flag+=1;
                }
            });
            rtPackage.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    Controllers.addRatingPackage(ratingBar.getRating());

                }
            });
            if(flag==3){
                float rate = (knowledge+personality+professional)/2;
                Controllers.addRatingTG(rate);
            }
        }

    }

    @Override
    public int getItemCount() {
        int size = 0;
        size = mData.size();
        return size;
    }

    @Override
    public RateAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_rating, parent, false);


        return new RateAdapter.MyViewHolder(view) {
        };
    }


    @Override
    public void onBindViewHolder(RateAdapter.MyViewHolder holder, int position) {
        if(position>0) {
            holder.imgTG.setImageResource(mData.get(position).getImage());
            holder.TGName.setText(mData.get(position).getName());
            holder.rtProfessional.setRating(0);
            holder.rtKnowledge.setRating(0);
            holder.rtPersonality.setRating(0);
            holder.rtPackage.setVisibility(View.GONE);
        }
        else{

            holder.imgTG.setImageResource(mData.get(position).getImage());
            holder.TGName.setText(mData.get(position).getName());
            holder.rtPackage.setRating(0);
            holder.txtKnowledge.setVisibility(View.GONE);
            holder.txtPersonality.setVisibility(View.GONE);
            holder.txtProfessional.setVisibility(View.GONE);
            holder.rtProfessional.setVisibility(View.GONE);
            holder.rtKnowledge.setVisibility(View.GONE);
            holder.rtPersonality.setVisibility(View.GONE);
        }

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