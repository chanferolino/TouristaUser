package com.touristadev.tourista.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.touristadev.tourista.R;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.CustomizedPackage;
import com.touristadev.tourista.dataModels.Spots;

import java.util.ArrayList;

public class SpotActivity extends AppCompatActivity {
    private int position;
    private Controllers mControllers = new Controllers();
    private Spots spotDetails;
    private ArrayList<Spots> pack = new ArrayList<>();
    private ImageView imgSpot;
    private Button btnBook;
    private RatingBar ratBar;
    private TextView mSpotName, mSpotAddress, mSpotBudget, mSpotOpen, mSpotClose, mSpotDesc;
    private String openTime, closeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        imgSpot = (ImageView) findViewById(R.id.TGimgSpot);
        ratBar = (RatingBar) findViewById(R.id.rtTGBar);
        mSpotName = (TextView) findViewById(R.id.txtTGTitle);
        mSpotAddress = (TextView) findViewById(R.id.txtTGSpotAddress);
        mSpotOpen = (TextView) findViewById(R.id.txtTGOpenTime);
        mSpotClose = (TextView) findViewById(R.id.txtTGCloseTime);
        mSpotDesc = (TextView) findViewById(R.id.txtTGSpotDesc);
        btnBook = (Button) findViewById(R.id.btnBook);
        Intent i = getIntent();
        position = i.getIntExtra("position", 0);
        pack = mControllers.getControllerSpots();
        spotDetails = pack.get(position);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controllers con = new Controllers();
                ArrayList<String> mList = new ArrayList<String>();
                ArrayList<CustomizedPackage> mCustomList = new ArrayList<CustomizedPackage>();
                mCustomList = con.getCustomizedPackageList();
                if(con.getCustomizedPackageList().size()<1){
                    final AlertDialog alertDialog = new AlertDialog.Builder(SpotActivity.this).create();

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Create new Package!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(SpotActivity.this, CustomPackageActivity.class);
                                    i.putExtra("title","null");
                                    startActivity(i);
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }else {


                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(SpotActivity.this);
                    builderSingle.setIcon(R.drawable.ic_media_pause);
                    builderSingle.setTitle("Add Spot to: ");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SpotActivity.this, android.R.layout.select_dialog_singlechoice);
                    for (int z = 0; z < mCustomList.size(); z++) {
                        arrayAdapter.add(mCustomList.get(z).getPackageName());
                    }
                    builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Controllers con = new Controllers();
                            final int[] wew = {which};
                            final String strName = arrayAdapter.getItem(which);
                            if (con.getCustomizedPackageList().size() < 3) {
                                AlertDialog.Builder builderInner = new AlertDialog.Builder(SpotActivity.this);
                                builderInner.setMessage(strName);
                                builderInner.setTitle("Added to " + strName);
                                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("SpotActivitychan", wew[0] +"");
                                        Controllers con = new Controllers();
                                        con.addSpotToPackage(spotDetails, wew[0]);
                                        Intent i = new Intent(SpotActivity.this, CustomPackageActivity.class);
                                        i.putExtra("title",strName);
                                        i.putExtra("pos",wew[0]+"");
                                        i.putExtra("type","details");
                                        startActivity(i);
                                        dialog.dismiss();
                                    }
                                });
                                builderInner.show();
                            }
                            else{
                                final AlertDialog.Builder builderInner = new AlertDialog.Builder(SpotActivity.this);
                                builderInner.setMessage("Can only add maximum of 3 Spots to "+strName);
                                builderInner.setTitle("Error ");
                                builderInner.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                    }
                                });
                                builderInner.show();
                            }
                        }
                    });
                    builderSingle.show();
                }
            }
        });


        imgSpot.setImageResource(spotDetails.getSpotImage());
        ratBar.setRating(spotDetails.getSpotRating());
        mSpotName.setText(spotDetails.getSpotName());
        mSpotAddress.setText("Address: " + spotDetails.getSpotAddress());
         openTime = spotDetails.getSpotOpeningTime();
            closeTime = spotDetails.getSpotClosingTime();

        mSpotOpen.setText("Open Time: " + openTime + " ");
        mSpotClose.setText("Close Time: " + closeTime + " ");
        mSpotDesc.setText("Description: " + "\n" + spotDetails.getSpotDescription());






        // ListView Item Click Listener

    }
}
