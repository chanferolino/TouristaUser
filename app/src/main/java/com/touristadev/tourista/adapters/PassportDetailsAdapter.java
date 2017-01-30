package com.touristadev.tourista.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.touristadev.tourista.R;
import com.touristadev.tourista.activities.BookDetailsActivity;
import com.touristadev.tourista.activities.ExploreActivity;
import com.touristadev.tourista.activities.TourActivity;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.models.CurrentUser;

import java.util.List;

import it.beppi.tristatetogglebutton_library.TriStateToggleButton;


public class PassportDetailsAdapter extends RecyclerView.Adapter<PassportDetailsAdapter.MyViewHolder> {


    public static Context mContext;
    private String ratingsCount, tripsCount, badgesCount;
    List<String> Squad;
    List<Integer> Trips;
    private TriStateToggleButton tgTGmode;
    int count;
    public static AppCompatActivity context;
    final QRCodeWriter writer = new QRCodeWriter();
    private String per;

    public   PassportDetailsAdapter(String ratingsCount, String tripsCount, String badgesCount, List<String> squad, List<Integer> trips,AppCompatActivity cone) {
        this.context = cone;
        this.ratingsCount = ratingsCount;
        this.tripsCount = tripsCount;
        this.badgesCount = badgesCount;
        this.Squad = squad;
        this.Trips = trips;

    }

    @Override
    public int getItemCount() {
        int size = 1;

        return size;
    }

    @Override
    public PassportDetailsAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_profiledetails, parent, false);


        return new PassportDetailsAdapter.MyViewHolder(view) {
        };
    }


    @Override
    public void onBindViewHolder(PassportDetailsAdapter.MyViewHolder holder, int position) {

        holder.mCardViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        holder.txtRatings.setText(ratingsCount);
        holder.txtTrips.setText(tripsCount);
        holder.txtBadges.setText(badgesCount);
//        FrameLayout.LayoutParams imageViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//        imageViewParams.gravity  = Gravity.RIGHT | Gravity.TOP;
        try {
            BitMatrix bitMatrix = writer.encode(CurrentUser.userFirebaseId, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            holder.imgCode.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView  mCardViewLogout;
        public TextView txtRatings, txtTrips, txtBadges;
        public ImageView imgCode;


        public MyViewHolder(final View v) {
            super(v);
            mContext = v.getContext();
            mCardViewLogout = (CardView) v.findViewById(R.id.cvLogOut);
            txtRatings = (TextView) v.findViewById(R.id.ratingsNumber);
            txtTrips = (TextView) v.findViewById(R.id.tripsNumber);
            txtBadges = (TextView) v.findViewById(R.id.badgesNumber);
            imgCode = (ImageView) v.findViewById(R.id.imgCode);

            imgCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();


                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                   alertDialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Share On Facebook!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final Bitmap bitmap = ((BitmapDrawable)imgCode.getDrawable()).getBitmap();
                                    SharePhoto photo = new SharePhoto.Builder()
                                            .setBitmap(bitmap)
                                            .build();
                                    SharePhotoContent content = new SharePhotoContent.Builder()
                                            .addPhoto(photo)
                                            .build();
                                    ShareApi.share(content,null);
                                    ShareDialog.show(context,content);
                                }
                            });
                    alertDialog.show();
                }
            });
        }
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        ;
    }
}
