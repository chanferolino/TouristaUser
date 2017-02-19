package com.touristadev.tourista.adapters;

/**
 * Created by Christian on 11/23/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.touristadev.tourista.activities.PackageDetailsActivity;
import com.touristadev.tourista.R;
import com.touristadev.tourista.activities.SpotActivity;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.TouristaPackages;
import com.touristadev.tourista.dataModels.Spots;
import com.touristadev.tourista.models.ExploreCard;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<ExploreCard> mData;
    private float mBaseElevation;
    private TextView txtTitle,txtPrice,txtSpots,txtHours,txtCompName;
    private ImageView imgVi;
    private RatingBar rtBar;
    private int pos;
    LayoutInflater mInflater;
    private Context context;
    private List<Bitmap> mImages;
    public  CardPagerAdapter(ArrayList<ExploreCard> Data) {

        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        mImages = new ArrayList();

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
                .inflate(R.layout.card_item_foryou, container, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

         mInflater = LayoutInflater.from(container.getContext());

        context = view.getContext();

        //FONTS
        Typeface FontPoppinsBold = Typeface.createFromAsset(container.getContext().getAssets(), "fonts/Poppins-Bold.ttf");
        Typeface FontPoppinsLight = Typeface.createFromAsset(container.getContext().getAssets(), "fonts/Poppins-Light.ttf");

        txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        txtPrice.setTypeface(FontPoppinsBold);

        txtTitle = (TextView) view.findViewById(R.id.txtTGTitle);
        txtTitle.setTypeface(FontPoppinsLight);

        txtSpots = (TextView) view.findViewById(R.id.txtNoSpots);
        txtSpots.setTypeface(FontPoppinsLight);

        txtHours = (TextView) view.findViewById(R.id.NoHours);
        txtHours.setTypeface(FontPoppinsLight);



        container.addView(view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        txtTitle = (TextView) view.findViewById(R.id.txtTGTitle);
        txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        txtSpots = (TextView) view.findViewById(R.id.txtNoSpots);
        txtHours = (TextView) view.findViewById(R.id.NoHours);
        rtBar = (RatingBar) view.findViewById(R.id.rtTGBar);
        imgVi = (ImageView) view.findViewById(R.id.imgCard);
        txtCompName = (TextView) view.findViewById(R.id.txtCompName);
        pos = position;

        imgVi.setImageResource(mData.get(position).getImgView());
        txtTitle.setText(mData.get(position).getTitle());
        txtPrice.setText(mData.get(position).getPrice());
        txtSpots.setText(mData.get(position).getNoSpots());
        txtHours.setText(mData.get(position).getNoHours());
        txtCompName.setText(mData.get(position).getCompanyName());
        rtBar.setRating((Float.parseFloat(String.valueOf(mData.get(position).getRating()))));
        rtBar.setFocusable(false);



        cardView.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        if(mData.get(position).getType().equals("spot")) {

                            Intent i = new Intent(context, SpotActivity.class);
                            i.putExtra("position",position);
                            context.startActivity(i);
                        }
                        else{
                            Intent i = new Intent(context, PackageDetailsActivity.class);

                            i.putExtra("fragtype", "non");
                            i.putExtra("position",position);
                            i.putExtra("type",mData.get(position).getType());
                            i.putExtra("title",mData.get(position).getTitle());
                            context.startActivity(i);
                        }

                    }







        });

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

}