package com.touristadev.tourista.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.touristadev.tourista.R;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.TouristaPackages;

import java.util.ArrayList;

public class PackageDetailsActivity extends AppCompatActivity {
    private int position;
    private ImageView imgPackage;
    private TextView txtPackageName,txtNumberSpots,txtNumberHours,txtPackPrice,txtPackDesc;
    private RatingBar ratBar;
    private ListView mListViewItinerary;
    private Button btnBook,btnPolicy;
    private TouristaPackages pack = new TouristaPackages();
    private ArrayList<TouristaPackages> mList = new ArrayList<>();
    private Controllers mControllers = new Controllers();

    private ArrayList<String> packItinerary = new ArrayList<>();
    private String typePackage,packageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);

        mList = mControllers.getControllerPackaaes();
        Intent i = getIntent();
        position = i.getIntExtra("position", 0);
        typePackage = i.getStringExtra("type");
        packageTitle = i.getStringExtra("title");
        imgPackage = (ImageView) findViewById(R.id.imgPackage);
        txtPackageName = (TextView) findViewById(R.id.txtTGTitle);
        txtNumberSpots = (TextView) findViewById(R.id.txtNumberSpot);
        txtNumberHours = (TextView) findViewById(R.id.txtNumberHours);
        txtPackPrice = (TextView) findViewById(R.id.txtPackPrice);
        txtPackDesc = (TextView) findViewById(R.id.txtPackDesc);
        btnPolicy = (Button) findViewById(R.id.btnCancellationPollicy);
        btnPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PackageDetailsActivity.this,PolicyActivity.class);
                startActivity(i);
            }
        });
        ratBar = (RatingBar) findViewById(R.id.rtTGBar);
        mListViewItinerary = (ListView) findViewById(R.id.PackageItineraryListView);
        btnBook = (Button) findViewById(R.id.btnBook);
        if (mList != null) {
            pack = mList.get(position);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PackageDetailsActivity.this, BooknowActivity.class);
                i.putExtra("position", position);
                i.putExtra("type", typePackage);
                i.putExtra("title", packageTitle);
                startActivity(i);
            }
        });
            imgPackage.setImageResource(pack.getPackageImage());
            txtPackageName.setText(pack.getPackageName());
            txtNumberSpots.setText(pack.getPackageNoOfSpots()+" Spots");
            txtNumberHours.setText(pack.getPackageTotalNoOfHours()+" Hours");
            txtPackPrice.setText("₱ "+pack.getPackPrice());
            txtPackDesc.setText(pack.getPackDescription());
            ratBar.setRating(pack.getRating());



            for (int x = 0; x < pack.getPackageItinerary().size(); x++) {
                packItinerary.add(pack.getPackageItinerary().get(x).getStartTime()+"\t\t\t\t "+pack.getPackageItinerary().get(x).getEndTime()+"\t\t\t\t "+pack.getPackageItinerary().get(x).getSpotID()+"\t\t\t\t");

            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, packItinerary);
            mListViewItinerary.setAdapter(adapter);


            ListAdapter listAdapter1 = mListViewItinerary.getAdapter();
            if (listAdapter1 != null) {

                int numberOfItems = listAdapter1.getCount();

                // Get total height of all items.
                int totalItemsHeight = 0;
                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                    View item = listAdapter1.getView(itemPos, null, mListViewItinerary);
                    item.measure(0, 0);
                    totalItemsHeight += item.getMeasuredHeight();
                }

                // Get total height of all item dividers.
                int totalDividersHeight = mListViewItinerary.getDividerHeight() *
                        (numberOfItems - 1);

                // Set list height.
                ViewGroup.LayoutParams params = mListViewItinerary.getLayoutParams();
                params.height = totalItemsHeight + totalDividersHeight;
                mListViewItinerary.setLayoutParams(params);
                mListViewItinerary.requestLayout();

            }



        }


    }
}
