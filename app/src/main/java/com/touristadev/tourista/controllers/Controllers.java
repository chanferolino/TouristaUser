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
import com.touristadev.tourista.models.CurrentUser;
import com.touristadev.tourista.models.TourGuideModel;
import com.touristadev.tourista.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Controllers {
    private static float ratingTg = 0;
    private static ArrayList<Spots> spotList = new ArrayList<>();
    private static ArrayList<Spots> finalSpotList = new ArrayList<>();
    private static String transactionID;
   private static ArrayList<TouristaPackages> packTemp = new ArrayList<>();
    private static ArrayList<TouristaPackages> packageList = new ArrayList<>();
    private static ArrayList<CustomizedPackage> customizedPackageList = new ArrayList<>();
    private static ArrayList<BookedPackages> BookedList = new ArrayList<>();
    private static String finurl = "http://192.168.1.4:8888/";
    private static ArrayList<TouristaPackages> RecentList = new ArrayList<>();
    private static FirebaseUser user;
    private static LatLng currentLocation;
    private static String currentUser;
    private static ArrayList<TouristaPackages> RequestList = new ArrayList<>();
    private static ArrayList<TouristaPackages> WishList = new ArrayList<>();
    private static ArrayList<TourGuideModel> tourguideList = new ArrayList<>();
    private static ArrayList<Spots> spotIt3 = new ArrayList<>();
    private static int positionwew;
    private static float ratPack;
   private static GoogleAccountCredential mCredentials = null;
    private static ArrayList<RatingTG> ratTG = new ArrayList<>();
    private static boolean tourguidemode;
    private static BookedPackages CurrPackage = new BookedPackages();
    private static ArrayList<TourGuideModel> currTourguideList = new ArrayList<>();

    public static void Controllers() {


    }
    public static void addCustomizedPackage(CustomizedPackage pack){
        customizedPackageList.add(pack);

    }
    public static void addRatingPackage(float ratingPack){
        ratPack = ratingPack;
    }
//    public static void addRatingTG(RatingTG rat){
//        ratTG.add(rat);
//    }
    public static void addRatingTG(Float rat){
        ratingTg = rat;
    }
    public static float getPackRate(){
        return ratPack;
    }
//    public static ArrayList<RatingTG> getRatingTG(){
//        return ratTG;
//    }
    public static float getRatingTG(){
        return ratingTg;
    }
    public static double CalculationByDistance(LatLng StartP, LatLng EndP) {
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
        Log.d("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return kmInDec;
    }
    public static void setCredentials(GoogleAccountCredential cred){
        mCredentials = cred;
    }
    public static GoogleAccountCredential getCredentials(){
        return mCredentials;
    }
    public static void addSpotToPackage(Spots spots,int pos){
        Log.d("SpotActivitychan","Controller : "+pos);
        Log.d("SpotActivitychan","Controller : "+customizedPackageList.size());

        Log.d("SpotActivitychan","Controller : "+ customizedPackageList.get(pos).getPackageItinerary().size());
        customizedPackageList.get(pos).getPackageItinerary().add(new Itinerary(spots.getSpotName(),spots.getSpotOpeningTime(),spots.getSpotClosingTime(),spots.getSpotName()));

    }
    public static void removeCustomizedPackage(int index){
        customizedPackageList.remove(index);

    }
    public static ArrayList<CustomizedPackage> getCustomizedPackageList(){
//        customizedPackageList.clear();
//        try {
//            new GetCustomPackageList().execute().get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        return customizedPackageList;
    }


    public static String getCurrentUserID(){

        return currentUser;
    }
    public static void setCurrentUserID(String id){

        currentUser = id;
    }
    public static ArrayList<TouristaPackages> getControllerPackaaes()
    {
        packageList.clear();
        try {
            new GetPackages().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.d("changwapo","packageList "+packageList.get(packageList.size()-1).getPackageItinerary().size());
        packTemp = packageList;
       return packageList;
    }

    public static ArrayList<Spots> getControllerSpots(){
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

        Log.d("changwapo","finalSpotList "+finalSpotList.size());
        return finalSpotList;
    }
    public static void addUser(FirebaseUser us)
    {
        user = us;


    }public static FirebaseUser getUser()
    {

        return user;

    }

    public static void removeWishPackage(int pos)
    {
        WishList.remove(pos);

    }
    public static void addWishPackage(TouristaPackages pos)
    {
        WishList.add(pos);

    }
    public static void addBookedPackages(BookedPackages pos)
    {
        BookedList.add(pos);

    }
    public static void removeBookedPackages(int pos)
    {
        BookedList.remove(pos);

    }
    public static void removeRequestPackage(int pos)
    {
        RequestList.remove(pos);

    }
    public static ArrayList<BookedPackages> getBookedList()
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


        Log.d("changwapo","BookedList "+BookedList.size());
        return BookedList;

    }
    public static ArrayList<TouristaPackages> getRecentList(){
        RecentList.clear();
        try {
            new GetRecentList().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("changwapo",e+" ");
        }


        Log.d("changwapo","Recent "+RecentList.size());
        return RecentList;
    }

    public static ArrayList<TouristaPackages> getWishList()
    {
        WishList.clear();
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
    public static ArrayList<TouristaPackages> getImage()
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
    public static ArrayList<TourGuideModel> getTourguideList(){


        return tourguideList;
    }
    public static void setPosition(int position2){
        positionwew = position2;
    }
    public static int getPosition(){
        return positionwew;
    }
    public static void setCurrentPackage(BookedPackages pack){

        pack = new BookedPackages();
        CurrPackage = pack;
    }
    public static void setCurrTourGuide(ArrayList<TourGuideModel> tgMod){
        currTourguideList.clear();
        for(int x = 0 ; x < tgMod.size();x++){
            currTourguideList.add(tgMod.get(x));
        }
        if(currTourguideList == null){
            currTourguideList = tgMod;
        }
    }
    public static BookedPackages getCurrentPackage(){

            return CurrPackage;

    }



    public static ArrayList<TourGuideModel> getCurrentTourguideList(){

        return tourguideList;

    }
    public static void postToDb(String url , JSONObject obj){
        HttpUtils.POST(finurl+""+url,obj);
    }
    public static void postToDb(String url , JSONArray obj){
        HttpUtils.POST(finurl+""+url,obj);
    }
    public static String getCurrentTransactionID(){

        return transactionID;
    }
   public static LatLng getCurrentLocation(){
       return currentLocation;
   }
    public static void setCurrentLocation(LatLng temp){
        currentLocation = temp;
    }



    static  class GetPackages extends AsyncTask<Void, Void, ArrayList<TouristaPackages>> {

        static  ArrayList<Itinerary> itineraries4 = new ArrayList<>();

        @Override
        protected ArrayList<TouristaPackages> doInBackground(Void... voids) {
            try {

                JSONArray json = new JSONArray(HttpUtils.GET(finurl+"api/get-best-tours"));
                for(int n = 0; n < json.length(); n++)
                {
                    JSONObject object = json.getJSONObject(n);
                    ArrayList<Spots> tempSpots = new ArrayList<>();
                    tempSpots = finalSpotList;
                    itineraries4.clear();
                    spotIt3.clear();
                    JSONArray jarray = object.getJSONArray("itineraries");
                    for(int z = 0 ; z < jarray.length();z++){
                        JSONObject j = jarray.getJSONObject(z);

                        itineraries4.add(new Itinerary(j.getString("spotName")+" "+j.getString("description"),j.getString("startTime"),j.getString("endTime"),j.getString("spotName")));
                        for(int y = 0 ; y < tempSpots.size() ; y++ ){
                            if(j.getString("spotName").equals(tempSpots.get(y).getSpotName())){
                                spotIt3.add(tempSpots.get(y));
                            }
                        }
                    }
                    packageList.add(new TouristaPackages(object.getString("packageId"),object.getString("packageName"),itineraries4,"Local",Integer.parseInt(object.getString("rating")),Integer.parseInt(object.getString("numOfSpots")),Integer.parseInt(object.getString("duration")),R.mipmap.ic_tourista,spotIt3,object.getString("description"),object.getString("payment"),object.getString("agencyName")));

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("changwapo",e+" package");
            }
            return packageList;
        }


    }
    static class GetCustomPackageList extends AsyncTask<Void, Void, ArrayList<TouristaPackages>> {

        ArrayList<Itinerary> itineraries4 = new ArrayList<>();

        @Override
        protected ArrayList<TouristaPackages> doInBackground(Void... voids) {
            Controllers con = new Controllers();
            try {
                JSONArray json = new JSONArray(HttpUtils.GET(finurl+"api/get-custom-package-tours?userId="+con.getCurrentUserID()+"&status=Request"));


                for(int n = 0; n < json.length(); n++)
                {
                    JSONObject object = json.getJSONObject(n);
                    JSONArray jarray = object.getJSONArray("itineraries");
                    for(int z = 0 ; z < jarray.length();z++){
                        JSONObject j = jarray.getJSONObject(z);

                        itineraries4.add(new Itinerary(j.getString("spotName")+" "+j.getString("description"),j.getString("startTime"),j.getString("endTime"),j.getString("spotName")));

                    }
//                    customizedPackageList.add(new CustomizedPackage(object.getString("packageId"),object.getString("packageName"),itineraries4,"Local",Integer.parseInt(object.getString("rating")),Integer.parseInt(object.getString("numOfSpots")),Integer.parseInt(object.getString("duration")),R.mipmap.ic_tourista,spotIt3,object.getString("description"),object.getString("payment")));
                    // do some stuff....
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("changwapo",e+" package");
            }
            return WishList;
        }

    }
    static class GetSpots extends AsyncTask<Void, Void, ArrayList<Spots>> {


        @Override
        protected ArrayList<Spots> doInBackground(Void... voids) {
            try {
                JSONArray json = new JSONArray(HttpUtils.GET(finurl+"api/get-featured-spots"));
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
    static class GetWishList extends AsyncTask<Void, Void, ArrayList<TouristaPackages>> {

        ArrayList<Itinerary> itineraries4 = new ArrayList<>();

        @Override
        protected ArrayList<TouristaPackages> doInBackground(Void... voids) {
            Controllers con = new Controllers();

            TouristaPackages tourTemp = new TouristaPackages();
            try {
                JSONArray json = new JSONArray(HttpUtils.GET(finurl+"api/get-booked-tours?userId="+con.getCurrentUserID()+"&status=Request"));


                for(int n = 0; n < json.length(); n++)
                {
                    JSONObject object = json.getJSONObject(n);
                    ArrayList<Spots> tempSpots = new ArrayList<>();
                    tempSpots = finalSpotList;
                    spotIt3.clear();
                    JSONArray jarray = object.getJSONArray("itineraries");
                    for(int z = 0 ; z < jarray.length();z++){
                        JSONObject j = jarray.getJSONObject(z);

                        itineraries4.add(new Itinerary(j.getString("spotName")+" "+j.getString("description"),j.getString("startTime"),j.getString("endTime"),j.getString("spotName")));
                        for(int y = 0 ; y < tempSpots.size() ; y++ ){
                            if(j.getString("spotName").equals(tempSpots.get(y).getSpotName())){
                                spotIt3.add(tempSpots.get(y));
                            }
                        }
                    }

                    WishList.add(new TouristaPackages(object.getString("packageId"),object.getString("packageName"),itineraries4,"Local",Integer.parseInt(object.getString("rating")),jarray.length(),Integer.parseInt(object.getString("duration")),R.mipmap.ic_tourista,spotIt3,object.getString("description"),object.getString("payment"),object.getString("agencyName")));
                    // do some stuff....
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("changwapo",e+" package");
            }
            return WishList;
        }

    }



    static class GetBookedTours extends AsyncTask<Void, Void, ArrayList<BookedPackages>> {

        ArrayList<Itinerary> itineraries4 = new ArrayList<>();

        @Override
        protected ArrayList<BookedPackages> doInBackground(Void... voids) {
            Controllers con = new Controllers();


            TouristaPackages tourTemp = new TouristaPackages();
            try {
                JSONArray json = new JSONArray(HttpUtils.GET(finurl+"api/get-booked-tours?userId="+con.getCurrentUserID()+"&status=Success"));

                for(int n = 0; n < json.length(); n++)
                {
                    JSONObject object = json.getJSONObject(n);
//                    transactionID = object.getString("tourTransactionId");
//                    for(int x = 0 ; x <packTemp.size() ; x++){
//                        if(object.getString("packageId").equals(packTemp.get(x).getPackageId())){
//                            tourTemp = packTemp.get(x);
//                            BookedList.add(new BookedPackages(tourTemp.getPackageId(),tourTemp.getPackageName(),tourTemp.getPackageItinerary(),tourTemp.getPackageTourGuideClassification(),tourTemp.getRating(),tourTemp.getPackageNoOfSpots(),tourTemp.getPackageTotalNoOfHours(),tourTemp.getPackageImage(),tourTemp.getSpotItinerary(),tourTemp.getPackDescription(),tourTemp.getPackPrice(),object.getString("tourDate"),tourTemp.getCompanyName()));
//                        }
//                    }
//
                    ArrayList<Spots> tempSpots = new ArrayList<>();
                    tempSpots = finalSpotList;
                    spotIt3.clear();
                    JSONArray jarray = object.getJSONArray("itineraries");
                    for(int z = 0 ; z < jarray.length();z++){
                        JSONObject j = jarray.getJSONObject(z);

                        itineraries4.add(new Itinerary(j.getString("spotName")+" "+j.getString("description"),j.getString("startTime"),j.getString("endTime"),j.getString("spotName")));
                        for(int y = 0 ; y < tempSpots.size() ; y++ ){
                            if(j.getString("spotName").equals(tempSpots.get(y).getSpotName())){
                                spotIt3.add(tempSpots.get(y));
                            }
                        }
                    }
                    JSONArray jarry = object.getJSONArray("guideDetails");
                    tourguideList.clear();
                    for(int x = 0 ; x < jarry.length();x++){
                        JSONObject je = jarry.getJSONObject(x);
                        tourguideList.add(new TourGuideModel(je.getString("firstName")+", "+je.getString("lastName"), R.mipmap.ic_launcher,je.getString("PROFILE_DESCRIPTION"),je.getInt("ratings"),je.getString("EMAIL"),je.getString("guideId")));

                    }
                    BookedList.add(new BookedPackages(object.getString("packageId"),object.getString("packageName"),itineraries4,"Local",Integer.parseInt(object.getString("rating")),jarray.length(),Integer.parseInt(object.getString("duration")),R.mipmap.ic_tourista,spotIt3,object.getString("description"),object.getString("payment"),object.getString("reserveDate"),object.getString("agencyName")));
                    // do some stuff....
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("changwapo",e+" package");
            }
            return BookedList;
        }

    }
    static class GetRecentList extends AsyncTask<Void, Void, ArrayList<TouristaPackages>> {

        ArrayList<Itinerary> itineraries4 = new ArrayList<>();

        @Override
        protected ArrayList<TouristaPackages> doInBackground(Void... voids) {
            Controllers con = new Controllers();
            try {
                JSONArray json = new JSONArray(HttpUtils.GET(finurl+"api/get-friends-activity?userId="+ CurrentUser.userFirebaseId));

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

                            itineraries4.add(new Itinerary(j.getString("spotName") + " " + j.getString("description"), j.getString("startTime"), j.getString("endTime"),j.getString("spotName")));

                        }
                        RecentList.add(new TouristaPackages(object.getString("packageId"), jobj.getString("packageName"), itineraries4, "Local", Integer.parseInt(jobj.getString("rating")), Integer.parseInt(jobj.getString("numOfSpots")), Integer.parseInt(jobj.getString("duration")), R.mipmap.ic_tourista, spotIt3, jobj.getString("description"), jobj.getString("payment"),jobj.getString("agencyName")));
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