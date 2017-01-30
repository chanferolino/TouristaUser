package com.touristadev.tourista.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.firebase.auth.FirebaseUser;
import com.touristadev.tourista.R;
import com.touristadev.tourista.dataModels.BookedPackages;
import com.touristadev.tourista.dataModels.Categories;
import com.touristadev.tourista.dataModels.CustomizedPackage;
import com.touristadev.tourista.dataModels.Itinerary;
import com.touristadev.tourista.dataModels.RatingTG;
import com.touristadev.tourista.dataModels.TouristaPackages;
import com.touristadev.tourista.dataModels.Spots;
import com.touristadev.tourista.dataModels.TourRequest;
import com.touristadev.tourista.dataModels.Tribes;
import com.touristadev.tourista.models.TourGuideModel;
import com.touristadev.tourista.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Controllers {

    private static ArrayList<Spots> spotList = new ArrayList<>();
    private static ArrayList<Spots> finalSpotList = new ArrayList<>();
    private static String transactionID;
    private static ArrayList<TouristaPackages> packageList = new ArrayList<>();
    private static ArrayList<CustomizedPackage> customizedPackageList = new ArrayList<>();
    private static ArrayList<BookedPackages> BookedList = new ArrayList<>();

    private static ArrayList<TouristaPackages> RecentList = new ArrayList<>();
    private static FirebaseUser user;
    private static LatLng currentLocation;
    private static String currentUser;
    private static ArrayList<TouristaPackages> RequestList = new ArrayList<>();
    private static ArrayList<TouristaPackages> WishList = new ArrayList<>();
    private static ArrayList<TourGuideModel> tourguideList = new ArrayList<>();
    private ArrayList<Spots> spotIt3 = new ArrayList<>();
    private static int positionwew;
    private static float ratPack;
    GoogleAccountCredential mCredentials;
    private static ArrayList<RatingTG> ratTG = new ArrayList<>();
    private static boolean tourguidemode;
    private static BookedPackages CurrPackage = new BookedPackages();
    private static ArrayList<TourGuideModel> currTourguideList = new ArrayList<>();

    public void Controllers() {


    }
    public void addCustomizedPackage(CustomizedPackage pack){
        customizedPackageList.add(pack);

    }
    public void addRatingPackage(float ratingPack){
        ratPack = ratingPack;
    }
    public void addRatingTG(RatingTG rat){
        ratTG.add(rat);
    }
    public float getPackRate(){
        return ratPack;
    }
    public ArrayList<RatingTG> getRatingTG(){
        return ratTG;
    }
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return km;
    }
    public void setCredentials(GoogleAccountCredential cred){
        mCredentials = cred;
    }
    public GoogleAccountCredential getCredentials(){
        return mCredentials;
    }
    public void addSpotToPackage(Spots spots,int pos){
        Log.d("SpotActivitychan","Controller : "+pos);
        Log.d("SpotActivitychan","Controller : "+customizedPackageList.size());

        Log.d("SpotActivitychan","Controller : "+ customizedPackageList.get(pos).getPackageItinerary().size());
        customizedPackageList.get(pos).getPackageItinerary().add(new Itinerary(spots.getSpotName(),spots.getSpotOpeningTime(),spots.getSpotClosingTime()));

    }
    public void removeCustomizedPackage(int index){
        customizedPackageList.remove(index);

    }
    public ArrayList<CustomizedPackage> getCustomizedPackageList(){
        return customizedPackageList;
    }


    public String getCurrentUserID(){

        return currentUser;
    }
    public void setCurrentUserID(String id){

        currentUser = id;
    }
    public ArrayList<TouristaPackages> getControllerPackaaes()
    {

        try {
            new GetPackages().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

       return packageList;
    }

    public ArrayList<Spots> getControllerSpots(){
        finalSpotList.clear();
//        ArrayList<String> activities0 = new ArrayList<>();
//        ArrayList<Categories> categories0 = new ArrayList<>();
//        ArrayList<Tribes> tribes0 = new ArrayList<>();
//
//        activities0.add("Swimming");
//
//        categories0.add(new Categories("Beaches and Resorts"));
//        categories0.add(new Categories("Nature"));
//
//        tribes0.add(new Tribes("Comformist"));
//        tribes0.add(new Tribes("Thrill- Seeker"));
//        tribes0.add(new Tribes("Self- Improver"));
//        spotList.clear();
//
//        finalSpotList.add(new Spots("0","Masters Resort Cebu","4044 Oslob, Cebu, Philippines",
//                "8:00","21:00","The southern part of the province is one of the areas where you can enjoy various sorts of seawater activities, do adventurous trips, have fun and cherish the tranquil ambiance and surroundings."
//                ,"9.459556960067692","123.37731275707483",4, R.mipmap.spot_oslobwhalshark));

        try {
            new GetSpots().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return finalSpotList;
    }
    public void addUser(FirebaseUser us)
    {
        user = us;


    }public FirebaseUser getUser()
    {

        return user;

    }

    public void removeWishPackage(int pos)
    {
        WishList.remove(pos);

    }
    public void removeBookedPackages(int pos)
    {
        BookedList.remove(pos);

    }
    public void removeRequestPackage(int pos)
    {
        RequestList.remove(pos);

    }
    public ArrayList<BookedPackages> getBookedList()
    {
        BookedList.clear();
        try {
            new GetBookedTours().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("changwapo",e+" ");
        }


        return BookedList;

    }
    public ArrayList<TouristaPackages> getRecentList(){

        try {
            new GetRecentList().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("changwapo",e+" ");
        }


        return RecentList;
    }

    public ArrayList<TouristaPackages> getWishList()
    {
        try {
            new GetWishList().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("changwapo",e+" ");
        }
        return WishList;
    }
    public ArrayList<TouristaPackages> getImage()
    {
        try {
            new GetWishList().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("changwapo",e+" ");
        }
        return WishList;
    }
    public ArrayList<TourGuideModel> getTourguideList(){
        tourguideList.clear();
        tourguideList.add(new TourGuideModel("Christian Ferolino", R.mipmap.ic_launcher, "Im gonna make you smile :)", 5, "18"));
        tourguideList.add(new TourGuideModel("Rey Manigos", R.mipmap.ic_launcher, "Your lovely companion :) ", 5, "17"));

        return tourguideList;
    }
    public static void setPosition(int position2){
        positionwew = position2;
    }
    public int getPosition(){
        return positionwew;
    }
    public void setCurrentPackage(BookedPackages pack){

        pack = new BookedPackages();
        CurrPackage = pack;
    }
    public void setCurrTourGuide(ArrayList<TourGuideModel> tgMod){
        currTourguideList.clear();
        for(int x = 0 ; x < tgMod.size();x++){
            currTourguideList.add(new TourGuideModel(tgMod.get(x)));
        }
        if(currTourguideList == null){
            currTourguideList = tgMod;
        }
    }
    public BookedPackages getCurrentPackage(){

            return CurrPackage;

    }



    public ArrayList<TourGuideModel> getCurrentTourguideList(){
        currTourguideList.clear();
        currTourguideList.add(new TourGuideModel("Christian Ferolino", R.mipmap.ic_launcher, "Im gonna make you smile :)", 5, "18"));
        currTourguideList.add(new TourGuideModel("Rey Manigos", R.mipmap.ic_launcher, "Your lovely companion :) ", 5, "17"));

        return currTourguideList;

    }
    public void postToDb(String url , JSONObject obj){
        HttpUtils.POST("http://25.88.64.96:2620/"+url,obj);
    }
    public void postToDb(String url , JSONArray obj){
        HttpUtils.POST("http://25.88.64.96:2620"+url,obj);
    }
    public String getCurrentTransactionID(){

        return transactionID;
    }
   public LatLng getCurrentLocation(){
       return currentLocation;
   }
    public void setCurrentLocation(LatLng temp){
        currentLocation = temp;
    }



    class GetPackages extends AsyncTask<Void, Void, ArrayList<TouristaPackages>> {

        ArrayList<Itinerary> itineraries4 = new ArrayList<>();

        @Override
        protected ArrayList<TouristaPackages> doInBackground(Void... voids) {
            try {
                JSONArray json = new JSONArray(HttpUtils.GET("http://25.88.64.96:2620/api/get-best-tours"));
                for(int n = 0; n < json.length(); n++)
                {
                    JSONObject object = json.getJSONObject(n);
                    JSONArray jarray = object.getJSONArray("itineraries");
                    for(int z = 0 ; z < jarray.length();z++){
                        JSONObject j = jarray.getJSONObject(z);

                        itineraries4.add(new Itinerary(j.getString("spotName")+" "+j.getString("description"),j.getString("startTime"),j.getString("endTime")));

                    }
                    packageList.add(new TouristaPackages(object.getString("packageId"),object.getString("packageName"),itineraries4,"Local",Integer.parseInt(object.getString("rating")),Integer.parseInt(object.getString("numOfSpots")),Integer.parseInt(object.getString("duration")),R.mipmap.ic_tourista,spotIt3,object.getString("description"),object.getString("payment")));
                    // do some stuff....
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("changwapo",e+" package");
            }
            return packageList;
        }


    }
    class GetSpots extends AsyncTask<Void, Void, ArrayList<Spots>> {


        @Override
        protected ArrayList<Spots> doInBackground(Void... voids) {
            try {
                JSONArray json = new JSONArray(HttpUtils.GET("http://25.88.64.96:2620/api/get-featured-spots"));
                for(int n = 0; n < json.length(); n++)
                {
                    JSONObject object = json.getJSONObject(n);

                    finalSpotList.add(new Spots(object.getString("spotId"),object.getString("spotName"),object.getString("streetAddress"),object.getString("opening"),object.getString("closing"),object.getString("description"),object.getString("LONGITUDE"),object.getString("LATITUDE"),5,R.mipmap.ic_launcher));
                }
            } catch (JSONException e) {
                e.printStackTrace();

                Log.d("changwapo",e+" spots");
            }
            return finalSpotList;
        }


    }
    class GetWishList extends AsyncTask<Void, Void, ArrayList<TouristaPackages>> {

        ArrayList<Itinerary> itineraries4 = new ArrayList<>();

        @Override
        protected ArrayList<TouristaPackages> doInBackground(Void... voids) {
            Controllers con = new Controllers();
            try {
                JSONArray json = new JSONArray(HttpUtils.GET("http://25.88.64.96:2620/api/get-best-tours?userId="+con.getCurrentUserID()+"&status=Request"));


                for(int n = 0; n < json.length(); n++)
                {
                    JSONObject object = json.getJSONObject(n);
                    JSONArray jarray = object.getJSONArray("itineraries");
                    for(int z = 0 ; z < jarray.length();z++){
                        JSONObject j = jarray.getJSONObject(z);

                        itineraries4.add(new Itinerary(j.getString("spotName")+" "+j.getString("description"),j.getString("startTime"),j.getString("endTime")));

                    }
                    WishList.add(new TouristaPackages(object.getString("packageId"),object.getString("packageName"),itineraries4,"Local",Integer.parseInt(object.getString("rating")),Integer.parseInt(object.getString("numOfSpots")),Integer.parseInt(object.getString("duration")),R.mipmap.ic_tourista,spotIt3,object.getString("description"),object.getString("payment")));
                    // do some stuff....
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("changwapo",e+" package");
            }
            return WishList;
        }

    }
    class GetBookedTours extends AsyncTask<Void, Void, ArrayList<BookedPackages>> {

        ArrayList<Itinerary> itineraries4 = new ArrayList<>();

        @Override
        protected ArrayList<BookedPackages> doInBackground(Void... voids) {
            Controllers con = new Controllers();
            try {
                JSONArray json = new JSONArray(HttpUtils.GET("http://25.88.64.96:2620/api/get-best-tours?userId="+con.getCurrentUserID()+"&status=Confirmed"));

                for(int n = 0; n < json.length(); n++)
                {
                    JSONObject object = json.getJSONObject(n);
                    transactionID = object.getString("tourTransactionId");

                    JSONArray jarray = object.getJSONArray("itineraries");
                    for(int z = 0 ; z < jarray.length();z++){
                        JSONObject j = jarray.getJSONObject(z);

                        itineraries4.add(new Itinerary(j.getString("spotName")+" "+j.getString("description"),j.getString("startTime"),j.getString("endTime")));

                    }
                    BookedList.add(new BookedPackages(object.getString("packageId"),object.getString("packageName"),itineraries4,"Local",Integer.parseInt(object.getString("rating")),Integer.parseInt(object.getString("numOfSpots")),Integer.parseInt(object.getString("duration")),R.mipmap.ic_tourista,spotIt3,object.getString("description"),object.getString("payment"),object.getString("reserveDate")));
                    // do some stuff....
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("changwapo",e+" package");
            }
            return BookedList;
        }

    }
    class GetRecentList extends AsyncTask<Void, Void, ArrayList<TouristaPackages>> {

        ArrayList<Itinerary> itineraries4 = new ArrayList<>();

        @Override
        protected ArrayList<TouristaPackages> doInBackground(Void... voids) {
            Controllers con = new Controllers();
            try {
                JSONArray json = new JSONArray(HttpUtils.GET("http://25.88.64.96:2620/api/get-friends-activity?userId=4WsRc7IsriQIyuA7zraN24Cgcl12"));

                for(int n = 0; n < json.length(); n++)
                {
                    JSONObject object = json.getJSONObject(n);
                    transactionID = object.getString("tourTransactionId");
                    JSONArray jary = object.getJSONArray("package");
                    for(int y = 0 ; y <jary.length();y++) {
                        JSONObject jobj = jary.getJSONObject(y);
                        JSONArray jarray = jobj.getJSONArray("itineraries");
                        for (int z = 0; z < jarray.length(); z++) {
                            JSONObject j = jarray.getJSONObject(z);

                            itineraries4.add(new Itinerary(j.getString("spotName") + " " + j.getString("description"), j.getString("startTime"), j.getString("endTime")));

                        }
                        RecentList.add(new TouristaPackages(object.getString("packageId"), jobj.getString("packageName"), itineraries4, "Local", Integer.parseInt(jobj.getString("rating")), Integer.parseInt(jobj.getString("numOfSpots")), Integer.parseInt(jobj.getString("duration")), R.mipmap.ic_tourista, spotIt3, jobj.getString("description"), jobj.getString("payment")));
                        // do some stuff....
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("changwapo",e+" package");
            }
            return RecentList;
        }

    }



}