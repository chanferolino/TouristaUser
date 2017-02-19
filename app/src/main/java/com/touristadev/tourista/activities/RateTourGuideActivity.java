package com.touristadev.tourista.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.touristadev.tourista.R;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.RatingTG;
import com.touristadev.tourista.dataModels.TouristaPackages;
import com.touristadev.tourista.fragments.RateFragment;
import com.touristadev.tourista.models.TourGuideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RateTourGuideActivity extends AppCompatActivity {

    private TouristaPackages currPack = new TouristaPackages();

    private ArrayList<TouristaPackages> PackageList = new ArrayList<>();
    private Controllers mControllers = new Controllers();
    private String typePackage,packageTitle;
    private Button btnOkay;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_tour_guide);
        Intent i = getIntent();
        position = i.getIntExtra("position", 0);
        typePackage = i.getStringExtra("type");
        packageTitle = i.getStringExtra("title");
        PackageList = mControllers.getControllerPackaaes();
        for(int x = 0;x<PackageList.size();x++){
            if(PackageList.get(x).getPackageName().equals(packageTitle)){
                currPack = PackageList.get(x);
            }
        }
        btnOkay = (Button) findViewById(R.id.btnOkay);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    JSONObject obj = new JSONObject();
                    JSONArray jarray = new JSONArray();
                    ArrayList<RatingTG> ratgList = new ArrayList<>();
                    float ratingTG= 0 ;
                    ratingTG = Controllers.getRatingTG();
//                    for(int x = 0 ; x < ratgList.size() ; x++){
//                        JSONObject j = new JSONObject();
//                        try {
////                    j.put("guideId",) addguideID
//                            j.put("acts_professionaly", ratgList.get(x).getPersonality());
//                            j.put("isknowledgable",ratgList.get(x).getKnowledge());
//                            j.put("rightpersonality",ratgList.get(x).getPersonality());
//                        } catch (JSONException e) {
//
//                            Log.d("chanRegisterActivity",e+"");
//                            e.printStackTrace();
//                        }
//                        jarray.put(j);
//                    }
                    try {
                        obj.put("package", Controllers.getPackRate());
                        obj.put("guide", ratingTG);
                    } catch (JSONException e) {

                        Log.d("RateTGactChan", e+"");
                        e.printStackTrace();
                    }
                    RateTourGuideActivity.POSTrating pr = new RateTourGuideActivity.POSTrating();
                    pr.execute(obj);


            }
        });

        if(savedInstanceState == null)
        {
            Fragment fragment;
            fragment = new RateFragment();
            Bundle args = new Bundle();
            args.putString("ARG_PARAM1", currPack.getPackageName());
            fragment.setArguments(args);
            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
            fr.add(R.id.fragment_container, fragment);
            fr.commit();
        }

    }
    public class POSTrating extends AsyncTask<JSONObject, Void, String> {

        @Override
        protected String doInBackground(JSONObject... params) {
            Controllers.postToDb("api/add-rating-to-tour-guide-and-package",params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String rt) {
            Intent i = new Intent(RateTourGuideActivity.this,ExploreActivity.class);
            startActivity(i);
            super.onPostExecute(rt);
        }
    }
}
