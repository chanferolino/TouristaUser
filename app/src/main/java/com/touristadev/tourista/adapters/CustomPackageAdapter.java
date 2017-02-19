package com.touristadev.tourista.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.touristadev.tourista.R;
import com.touristadev.tourista.activities.BookDetailsActivity;
import com.touristadev.tourista.activities.CustomPackageActivity;
import com.touristadev.tourista.activities.SpotActivity;
import com.touristadev.tourista.activities.PackageDetailsActivity;
import com.touristadev.tourista.activities.TourActivity;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.CustomizedPackage;
import com.touristadev.tourista.dataModels.TouristaPackages;
import com.touristadev.tourista.models.ExploreCard;

import java.util.ArrayList;
import java.util.List;

public class CustomPackageAdapter extends RecyclerView.Adapter<CustomPackageAdapter.MyViewHolder>{

    private List<CardView> mViews;
    private static List<CustomizedPackage> mDataAda;
    private List<Bitmap> mImages;
    private Context context;

    public static int pos;
    private TextView txtTitle,txtPrice,txtSpots,txtHours;

    public  CustomPackageAdapter(ArrayList<CustomizedPackage> Data) {

        mDataAda = new ArrayList<>();
        mViews = new ArrayList<>();
        mImages = new ArrayList();

        if(Data!=null ){
            for (int i = 0; i < Data.size(); i++) {
                mDataAda.add(Data.get(i));
                mViews.add(null);

            }
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtTitle;
        public TextView txtPrice;
        public TextView txtSpots;
        public TextView txtDate;
        public ImageView imageV;
        public CardView cardView;
        public Controllers mControllers;
        public MyViewHolder(View v) {
            super(v);
            View view = v;
            cardView = (CardView) view.findViewById(R.id.cardView);
            txtTitle = (TextView) view.findViewById(R.id.txtTGTitle);
            txtPrice = (TextView) view.findViewById(R.id.txtPrice);
            txtSpots = (TextView) view.findViewById(R.id.txtNoSpots);
            txtDate = (TextView) view.findViewById(R.id.tourDate);
            imageV = (ImageView) view.findViewById(R.id.imgCard);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            pos = getAdapterPosition();
            Log.d("CusstomAdapter", "Position "+pos);
            Controllers.setPosition(getAdapterPosition());
            afterClick(view);
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        size = mDataAda.size();
        return size;
    }

    @Override
    public CustomPackageAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_customized, parent, false);
        context = view.getContext();

        //FONTS
//        Typeface FontPoppinsBold = Typeface.createFromAsset(context.getAssets(), "fonts/Poppins-Bold.ttf");
//        Typeface FontPoppinsLight = Typeface.createFromAsset(context.getAssets(), "fonts/Poppins-Light.ttf");
//
//        txtPrice = (TextView) view.findViewById(R.id.txtPrice);
//        txtPrice.setTypeface(FontPoppinsBold);
//
//        txtTitle = (TextView) view.findViewById(R.id.txtTGTitle);
//        txtTitle.setTypeface(FontPoppinsLight);
//
//        txtSpots = (TextView) view.findViewById(R.id.txtNoSpots);
//        txtSpots.setTypeface(FontPoppinsLight);
//
//        txtHours = (TextView) view.findViewById(R.id.NoHours);
//        txtHours.setTypeface(FontPoppinsLight);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {





            }
        });
        return new CustomPackageAdapter.MyViewHolder(view) {
        };
    }


    @Override
    public void onBindViewHolder(CustomPackageAdapter.MyViewHolder holder, int position) {
        holder.cardView.setTag(position);
        holder.imageV.setImageResource(mDataAda.get(position).getPackageImage());
        holder.txtTitle.setText(mDataAda.get(position).getPackageName());
        holder.txtPrice.setText("₱ "+mDataAda.get(position).getPrice());
        if(mDataAda.get(position).getPackageItinerary()==null){
            holder.txtSpots.setText("No itinerary yet");
        }
        else {
            holder.txtSpots.setText(mDataAda.get(position).getPackageItinerary().size()+" Spots");
        }
        holder.txtDate.setText("Date: "+mDataAda.get(position).getStartDate());
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    public static void afterClick(final View v){


            AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "View Details",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(v.getContext(), CustomPackageActivity.class);
                            i.putExtra("type","details");
                            i.putExtra("pos",String.valueOf(pos));
                            v.getContext().startActivity(i);
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Controllers.removeWishPackage(pos);
                            Intent i = new Intent(v.getContext(), TourActivity.class);
                            v.getContext().startActivity(i);
                        }
                    });
            alertDialog.show();

        }


    }




