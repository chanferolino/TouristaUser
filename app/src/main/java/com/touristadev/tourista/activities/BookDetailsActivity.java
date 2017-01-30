package com.touristadev.tourista.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.roughike.bottombar.BottomBar;
import com.touristadev.tourista.Parser.DataParser;
import com.touristadev.tourista.R;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.BookedPackages;
import com.touristadev.tourista.dataModels.TouristaPackages;
import com.touristadev.tourista.fragments.TourGuideDetailsFragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookDetailsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleMap mMap;
    private int position;
    private String typePackage,packageTitle;
    private ArrayList<BookedPackages> packList = new ArrayList<>();
    private BookedPackages packageBook = new BookedPackages();
    private Controllers con = new Controllers();
    private ArrayList<LatLng> points = new ArrayList<>();
    private  PolylineOptions lineOptions =new PolylineOptions();
    private  PolylineOptions finalOptions =new PolylineOptions();
    private int r=0;
    private               ArrayList <LatLng> booked = new ArrayList<LatLng>();
    private Button btnEnd,btnViewTour,btnAddComment;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        btnEnd = (Button) findViewById(R.id.btnEnd);
        btnEnd.setEnabled(true);
        btnViewTour = (Button) findViewById(R.id.btnViewTour);
        btnViewTour.setEnabled(true);
        btnAddComment = (Button) findViewById(R.id.btnAddComment);
        btnAddComment.setEnabled(true);
        Intent i = getIntent();
        position = i.getIntExtra("position", 0);
        typePackage = i.getStringExtra("type");
        packageTitle = i.getStringExtra("title");

        packList = con.getBookedList();
        for(int x = 0 ; x<packList.size();x++){

            if(packList.get(x).getPackageName().equals(packageTitle)){
                packageBook = packList.get(x);

                con.setCurrentPackage(packageBook);
            }
        }
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int x = 0 ; x<packList.size();x++){

                    if(packList.get(x).getPackageName().equals(packageTitle)){
                        packageBook = packList.get(x);

                        con.setCurrentPackage(packageBook);
                    }
                }
                Intent i = new Intent(BookDetailsActivity.this,RateTourGuideActivity.class);
                i.putExtra("position", position);
                i.putExtra("type", typePackage);
                i.putExtra("title", packageTitle);
                    startActivity(i);

            }
        });
        btnViewTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookDetailsActivity.this,PackageDetailsActivity.class);
                i.putExtra("position", position);
                i.putExtra("type", typePackage);
                i.putExtra("title", packageTitle);
                startActivity(i);
            }
        });
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookDetailsActivity.this);
                LayoutInflater inflater = BookDetailsActivity.this.getLayoutInflater();
                View layout = inflater.inflate(R.layout.alertdialog_book_details    /*my layout here*/, null);
                builder.setView(layout);
                builder.show();
            }
        });
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment supportMapFragment =  SupportMapFragment.newInstance();
        fm.beginTransaction().replace(R.id.map, supportMapFragment).commit();

        supportMapFragment.getMapAsync(this);

        if(savedInstanceState == null)
        {
            for(int x = 0 ; x<packList.size();x++){
                if(packList.get(x).getPackageName().equals(packageTitle)){
                    packageBook = packList.get(x);

                    con.setCurrentPackage(packageBook);
                }
            }
            Fragment fragment;
            fragment = new TourGuideDetailsFragment();
            Bundle args = new Bundle();
            args.putString("ARG_PARAM1", packageTitle);
            fragment.setArguments(args);
            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
            fr.add(R.id.fragment_container, fragment);
            fr.commit();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);

            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }



                MarkerOptions options = new MarkerOptions();



              for(int x = 0 ; x<packageBook.getSpotItinerary().size();x++){
                  mMap.addMarker(options.position(new LatLng(Double.parseDouble(packageBook.getSpotItinerary().get(x).getSpotLocationLong()),Double.parseDouble(packageBook.getSpotItinerary().get(x).getSpotLocationLat()))));
                    booked.add(new LatLng(Double.parseDouble(packageBook.getSpotItinerary().get(x).getSpotLocationLong()),Double.parseDouble(packageBook.getSpotItinerary().get(x).getSpotLocationLat())));
                  options.icon(getMarkerIcon("#fecd23"));

                }


            LatLng origin = null;
            LatLng dest = null;
            int flag=1;
                    for(int z = 0 ; z <booked.size();z++) {
                        int wew=0;
                         origin = booked.get(z);
                        wew=z+1;
                        if(wew!=booked.size()) {

                            dest = booked.get(wew);
                        }
                        else{
                          flag=0;
                        }
                        if(flag==1) {
                            String url = getUrl(origin, dest);
                            FetchUrl FetchUrl = new FetchUrl();
                            FetchUrl.execute(url);


                        }
                        r = z;
                    }

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(booked.get(0),10));

    }
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);}
private class FetchUrl extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... url) {
        String data = "";

        try {
            data = downloadUrl(url[0]);
        } catch (Exception e) {
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserTask parserTask = new ParserTask();
        parserTask.execute(result);

    }
}
    private String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();

                // Starts parsing data
                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            for (int i = 0; i < result.size(); i++) {
                List<HashMap<String, String>> path = result.get(i);
                for(int x = 0 ; x<booked.size();x++) {
                    points.clear();
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);

                    }
                }
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);


            }

            if(lineOptions != null) {
                    finalOptions.addAll(lineOptions.getPoints());
                mMap.addPolyline(lineOptions);



            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        con.setCurrentLocation(latLng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        int size = packageBook.getSpotItinerary().size();
        if(mCurrLocationMarker.getPosition().equals(new LatLng(Double.parseDouble(packageBook.getSpotItinerary().get(size-1).getSpotLocationLat()),Double.parseDouble(packageBook.getSpotItinerary().get(size-1).getSpotLocationLong()))))
        {
            btnEnd.setEnabled(true);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
}

