package com.touristadev.tourista.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.touristadev.tourista.R;
import com.touristadev.tourista.models.CityMaps;

import java.util.ArrayList;
import java.util.Random;


public class DiscoverActivity extends AppCompatActivity implements OnMapReadyCallback {
    BottomBar mBottomBar;
    private GoogleMap mMap;
    private int noStars;
    private ArrayList<CityMaps> city = new ArrayList<>();
    private TextView txt_Description,txtTile;
    private RatingBar ratBarM;
    private Button btnView;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent= new Intent(DiscoverActivity.this,ExploreActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    private MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_discover);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#fecd23"));
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for(int x = 0 ; x < city.size() ; x++){
                    if(query.equals(city.get(x).getCityName())){

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city.get(x).getLatlng(),8));
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        mBottomBar= BottomBar.attach(this,savedInstanceState);
        mBottomBar.useFixedMode();
        mBottomBar.setTypeFace("fonts/Poppins-Regular.ttf");
        mBottomBar.setActiveTabColor(Color.parseColor("#fecd23"));
        mBottomBar.setDefaultTabPosition(1);
        mBottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if(menuItemId== R.id.bottombar1)
                {
                   Intent i = new Intent(DiscoverActivity.this, ExploreActivity.class);
                    startActivity(i);
                }
                if(menuItemId== R.id.bottombar3)
                {

                    Intent i = new Intent(DiscoverActivity.this, TourActivity.class);
                    startActivity(i);
                }
                if(menuItemId== R.id.bottombar4)
                {
                    Intent i = new Intent(DiscoverActivity.this, PassportActivity.class);
                    i.putExtra("tourguidemode",false);
                    startActivity(i);
                }
            }


            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });
        mBottomBar.setDefaultTabPosition(1);

    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }





    private void setUpSpots(GoogleMap googleMap) {

        city.add(new CityMaps("Manila City",new LatLng(14.5995,120.9842),340,"Manila, the capital of the Philippines, is a densely populated bayside city on the island of Luzon",40));
        city.add(new CityMaps("Cebu City",new LatLng(10.3157,123.8854),453,"Cebu is a province of the Philippines, in the country’s Central Visayas region, comprising Cebu Island and more than 150 smaller surrounding islands and islets.",54));
        city.add(new CityMaps("Cagayan De Oro City",new LatLng(8.4542,124.6319),254,"Cagayan de Oro is a first class highly urbanized city and the capital of the province of Misamis Oriental",38));
        city.add(new CityMaps("Iloilo City",new LatLng(10.7202,122.5621),325,"Iloilo City, officially the City of Iloilo, is a highly urbanized city on Panay island in the Philippines.",43));
        city.add(new CityMaps("Bacolod City",new LatLng(10.6407,122.9690),299,"Bacolod City is one of the best destinations in the Philippines. Great food, peaceful and serene community, easy access to basic needs, hospitable people",39));
        city.add(new CityMaps("Camarines",new LatLng(13.5250,123.3486),354,"Camarines Sur is a province located in the Bicol Region in Luzon of the Philippines",36));
        city.add(new CityMaps("Zamboanga City",new LatLng(6.92,122.08),412,"Zamboanga City is a highly urbanized city located in Mindanao, Philippines.",43));
        city.add(new CityMaps("Baguio City",new LatLng(16.4023,120.5960),304,"Baguio, on the Philippines’ Luzon island, is a mountain town of universities and resorts. Called the “City of Pines,” ",31));
        city.add(new CityMaps("Legazpi City",new LatLng(13.1391,123.7438),389,"Legazpi, officially the City of Legazpi and often referred to as Legazpi City, is a component city and the capital of the province of Albay in the Philippines",39));
        city.add(new CityMaps("Samar City",new LatLng(11.9325,125.0388),452,"Samar is an island in the Visayas, within central Philippines and the third largest island in the country. The island is divided into three provinces: Samar province, Northern Samar province, and Eastern Samar province.",61));
        city.add(new CityMaps("Bohol City",new LatLng(9.8500,124.1435),467,"Bohol is known for coral reefs and unusual geological formations, notably the Chocolate Hills. ",31));
        city.add(new CityMaps("Pasay City",new LatLng(14.5378,121.0014),501,"Pasay is one of the cities in Metro Manila, the National Capital Region of the Philippines.",19));
        city.add(new CityMaps("Quezon City",new LatLng(14.6760,121.0437),425,"Quezon City is the most populous city in the Philippines. It is one of the cities that make up Metro Manila, the National Capital Region of the Philippines.",22));
        city.add(new CityMaps("Ilocos Norte City",new LatLng(18.1647,120.7116),430,"is a province of the Philippines located in the Ilocos Region.",58));
        city.add(new CityMaps("Lapu Lapu City",new LatLng(10.2662,123.9973),441,"Lapu-Lapu, officially the City of Lapu-Lapu and formerly called Opon, is a 1st city income class highly urbanized city in the region of Central Visayas, Philippines.",18));
        city.add(new CityMaps("Basilan City",new LatLng(6.4296,121.9870),406,"Basilan is an island province of the Philippines within the Autonomous Region in Muslim Mindanao. ",29));
        city.add(new CityMaps("Isabela City",new LatLng(6.7029,121.9690),398,"Isabela, officially the City of Isabela, and often referred to as Isabela City, is a 4th class city and the capital of the province of Basilan, Philippines.",25));
        city.add(new CityMaps("Puerto Princesa ",new LatLng(9.9672,118.7855),387,"Puerto Princesa is a coastal city on Palawan Island in the western Philippines. It's a base for boat trips through the massive limestone caves and underground river of the biodiverse Puerto Princesa Subterranean River National Park. Home to long-nosed dolphins, turtles and rays.",34));
        city.add(new CityMaps("Butuan City",new LatLng(8.9475,125.5406),510,"Butuan, officially the City of Butuan and often referred to as Butuan City, is a highly urbanized city in the Philippines and the regional center of Caraga.",39));
        city.add(new CityMaps("Cotabato City",new LatLng(7.2047,124.2310),407,"Cotabato City, officially the City of Cotabato, is a City in the Philippines located in Mindanao, Philippines. According to the 2015 census, it has a population of 299,438.",41));
        city.add(new CityMaps("Makati City",new LatLng(14.5547,121.0244),433,"Makati is a city in the Philippines’ Metro Manila region and the country’s financial hub. It’s known for the skyscrapers and shopping malls of Makati Central Business District, and for Ayala Triangle Gardens,",40));
        city.add(new CityMaps("Angeles City",new LatLng(15.1450,120.5887),468,"Angeles is a highly urbanized city located geographically within the province of Pampanga in the Philippines.",43));
        city.add(new CityMaps("Davao City",new LatLng(7.1907,125.4553),490,"Davao City, on the southern Philippine island of Mindanao, is a coastal commercial center near 2,954m-high Mount Apo, the country’s highest peak. In the city center, People’s Park is known for its colorful indigenous sculptures and lighted fountains. It's also home to Durian Dome, named after the pungent, spiky fruit that grows in abundance on Mindanao. The Davao River cuts through the city.",49));
        city.add(new CityMaps("Batangas City",new LatLng(13.7565,121.0583),512,"Batangas City, is the largest and capital city of the Province of Batangas, Philippines. Known as the \"Industrial Port City of Calabarzon\", Batangas City is currently classified as one of the fastest urbanizing cities of the Philippines.",41));
        city.add(new CityMaps("General Santos",new LatLng(6.1164,125.1716),543,"Home of the Best Filipino boxer, General santos The city is southeast of Manila, southeast of Cebu and southwest of Davao. The city is bounded by municipalities of Sarangani Province namely Alabel in the east, and Maasim in the south.",59));
        city.add(new CityMaps("Valenzuela City",new LatLng(14.7011,120.9830),501,"Valenzuela, officially the City of Valenzuela or simply Valenzuela City, is the 119th largest city in the Philippines",51));
        city.add(new CityMaps("Tacloban City",new LatLng(11.2543,124.9617),501,"is a 1st city income class highly urbanized city in the Philippines and the provincial capital of Leyte where it is geographically situated but governed administratively independent from it, Was recently struct by typhoon Yolanda.",32));


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }
        setUpSpots(mMap);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(12.8797, 121.7740),5));

        for(int x = 0 ; x < city.size(); x++) {
            mMap.addMarker(new MarkerOptions().position(city.get(x).getLatlng())
                    .title(city.get(x).getCityName())
                    .snippet("\t\t"+city.get(x).getRating()+"\t\tRatings \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+city.get(x).getNoSpots()+" Spots\n"+"\n"+"Description: "+"\n"+city.get(x).getDescription())
                    .icon(getMarkerIcon("#fecd23")));

        }
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Intent i = new Intent(DiscoverActivity.this,PackageListActivity.class);
                startActivity(i);

            }

        });
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {



            @Override
            public View getInfoWindow(Marker arg0) {

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

                txtTile = (TextView) v.findViewById(R.id.txtTourPackageName);
                txt_Description = (TextView) v.findViewById(R.id.txtDe) ;
                ratBarM = (RatingBar) v.findViewById(R.id.rtBarA);
                btnView = (Button) v.findViewById(R.id.btnView);

                Random r = new Random();
                int stars = r.nextInt(6 - 4) + 4;
                noStars = stars;

                txtTile.setText(marker.getTitle());
                txt_Description.setText(marker.getSnippet());
                ratBarM.setRating((Float.parseFloat(String.valueOf(noStars))));
                ratBarM.setFocusable(false);

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent i = new Intent(DiscoverActivity.this,PackageListActivity.class);
                        i.putExtra("City",marker.getTitle());
                        startActivity(i);
                    }
                });
                return v;
            }
        });
    }
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);}
}
