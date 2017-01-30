package com.touristadev.tourista.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
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
    private Controllers con = new Controllers();
    private Controllers mControllers = new Controllers();
    private String typePackage,packageTitle;
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
        if(con.getPackRate()!=0&&con.getRatingTG().size()!=0) {


            JSONObject obj = new JSONObject();
            JSONArray jarray = new JSONArray();
            ArrayList<RatingTG> ratgList = new ArrayList<>();
            ratgList = con.getRatingTG();
            for(int x = 0 ; x < ratgList.size() ; x++){
                JSONObject j = new JSONObject();
                try {
//                    j.put("guideId",) addguideID
                    j.put("acts_professionaly", ratgList.get(x).getPersonality());
                    j.put("isknowledgable",ratgList.get(x).getKnowledge());
                    j.put("rightpersonality",ratgList.get(x).getPersonality());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("chanRegisterActivity",e+"");
                }
                jarray.put(j);
            }
            try {
                obj.put("packageId", "customized");
                obj.put("rating", con.getPackRate());
                obj.put("tourTransactionId", con.getCurrentTransactionID());
                obj.put("guide", jarray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RateTourGuideActivity.POSTrating pr = new RateTourGuideActivity.POSTrating();
            pr.execute(obj);
        }
    }
    public class POSTrating extends AsyncTask<JSONObject, Void, String> {

        @Override
        protected String doInBackground(JSONObject... params) {
            Controllers con = new Controllers();
            con.postToDb("/api/add-rating-to-tour-guide-and-package",params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String rt) {

            super.onPostExecute(rt);
        }
    }
}
