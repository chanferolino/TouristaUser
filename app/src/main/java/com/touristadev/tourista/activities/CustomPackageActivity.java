package com.touristadev.tourista.activities;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.touristadev.tourista.R;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.CustomizedPackage;
import com.touristadev.tourista.dataModels.Itinerary;
import com.touristadev.tourista.dataModels.Spots;
import com.touristadev.tourista.dataModels.TouristaPackages;
import com.touristadev.tourista.models.CurrentUser;
import com.touristadev.tourista.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CustomPackageActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,  LocationListener {
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    static final int RESULT_LOAD_IMAGE = 1;

    GoogleAccountCredential mFinalCredential;
    private TouristaPackages pack = new TouristaPackages();
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR };
    private EditText edtDate,edtDescription;
    private TextView txtPackName,txtDays,txtPrice;
    private MaterialSpinner spinLang,spinNumTG,spinPax;
    private ListView itineraryList;
    private Button btnBook;
    private String spinTGnum,spinLanguage,spinPaxnum;
    private Calendar myCalendar;
    private String type;
    private String eventDate;
    private ImageView imgV;
    private String pos;
    private Controllers con = new Controllers();
    private ArrayList<CustomizedPackage> customList = new ArrayList<>();
    private CustomizedPackage customData = new CustomizedPackage();
    private LocationManager mLocationManager;
    private ArrayList<String> packItinerary = new ArrayList<>();
    private ListView mListViewItinerary;
    private LocationListener mLocationListener;
    ProgressDialog mProgress;
    GoogleApiClient mGoogleApiClient;
    private LatLng curlocation;
    LocationRequest mLocationRequest;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;


    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_custom_package);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        edtDate = (EditText) findViewById(R.id.edtCustomDate);
        edtDescription = (EditText) findViewById(R.id.customDesc);
        txtPackName = (TextView) findViewById(R.id.txtCustomPackName);
        txtPackName.setFocusable(true);
        txtPackName.setEnabled(true);
        txtPackName.setClickable(true);
        txtDays = (TextView) findViewById(R.id.txtCustomDays);
        txtDays.setFocusable(true);
        txtDays.setEnabled(true);
        txtDays.setClickable(true);
        txtPrice = (TextView) findViewById(R.id.txtCustomPrice);
        txtPrice.setFocusable(true);
        txtPrice.setEnabled(true);
        txtPrice.setClickable(true);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);
        }
        spinLang = (MaterialSpinner) findViewById(R.id.spinCustomLang);
        spinNumTG = (MaterialSpinner) findViewById(R.id.spnCustomTourGuides);
        spinPax = (MaterialSpinner) findViewById(R.id.spinPax);
        spinNumTG.setItems("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

        spinNumTG.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                spinTGnum = item;
            }
        });
        spinPax.setItems("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

        spinPax.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                spinPaxnum = item;
            }
        });

        spinLang.setItems("English", "Cebuano", "Tagalog", "Japanese", "Chinese", "Korean", "Bicolano", "Waray");
        spinLang.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                spinLanguage = item;
            }
        });
        itineraryList = (ListView) findViewById(R.id.CustomPackageItineraryListView);
        btnBook = (Button) findViewById(R.id.btnBook);
        imgV = (ImageView) findViewById(R.id.imageView3);
        imgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        Intent i = getIntent();
        type = i.getStringExtra("type");
        edtDate.setFocusable(false);
        edtDescription.setFocusable(false);
        spinLang.setEnabled(false);
        spinPax.setEnabled(false);
        spinNumTG.setEnabled(false);
        imgV.setOnClickListener(null);
        txtPackName.setFocusable(false);
        txtPackName.setEnabled(false);
        txtPackName.setClickable(false);
        txtPackName.setFocusableInTouchMode(false);
        txtDays.setFocusable(false);
        txtDays.setEnabled(false);
        txtDays.setClickable(false);
        txtDays.setFocusableInTouchMode(false);
        txtPrice.setFocusable(false);
        txtPrice.setEnabled(false);
        txtPrice.setClickable(false);
        txtPrice.setFocusableInTouchMode(false);

        Log.d("CustomPackageChannix","Type: "+type);
        if(type.equals("create")){
            pos = "0";

            btnBook.setVisibility(View.GONE);
            edtDate.setFocusable(true);
            edtDescription.setFocusable(true);
            edtDescription.setFocusableInTouchMode(true);
            spinLang.setEnabled(true);
            spinNumTG.setEnabled(true);
            spinPax.setEnabled(true);
            imgV.setOnClickListener(null);
            txtPackName.setFocusable(true);
            txtPackName.setEnabled(true);
            txtPackName.setClickable(true);
            txtPackName.setFocusableInTouchMode(true);
            txtDays.setFocusable(true);
            txtDays.setEnabled(true);
            txtDays.setClickable(true);
            txtDays.setFocusableInTouchMode(true);
            txtPrice.setFocusable(true);
            txtPrice.setEnabled(true);
            txtPrice.setClickable(true);
            txtPrice.setFocusableInTouchMode(true);
        }
        if(type.equals("details")){
            pos = i.getStringExtra("pos");
            Log.d("CustomPackagechan",pos+"");
            Log.d("CustomPackagechan",i.getStringExtra("pos"));
            customList = con.getCustomizedPackageList();
            customData = customList.get(Integer.parseInt(pos));
            Log.d("CustomPackagechan",Integer.parseInt(pos)+"");


            edtDate.setText(customData.getStartDate());
            edtDescription.setText(customData.getPackDescription());
            txtPackName.setText(customData.getPackageName());
            txtDays.setText(Integer.toString(customData.getNumberOfDays()));
            txtPrice.setText(customData.getPrice());
            imgV.setImageResource(customData.getPackageImage());
            int spinlangIndex = 0;
            int spinNumTGindex = 0;
            int spinNumPaxindex = 0;
            for(int x = 0 ; x<spinLang.getItems().size();x++){
                if(spinLang.getItems().get(x).equals(customData.getPackLanguage())){
                    spinlangIndex = x;
                }
            }
            spinLang.setSelectedIndex(spinlangIndex);
            spinLang.setEnabled(false);
            for(int x = 0 ; x<spinNumTG.getItems().size();x++){
                if(spinNumTG.getItems().get(x).equals(customData.getPackNumTourGuide())){
                    spinNumTGindex = x;
                }
            }
            spinNumTG.setSelectedIndex(spinNumTGindex);
            spinNumTG.setEnabled(false);
            for(int x = 0 ; x<spinPax.getItems().size();x++){
                if(spinPax.getItems().get(x).equals(customData.getPackNumTourGuide())){
                    spinNumPaxindex = x;
                }
            }
            spinPax.setSelectedIndex(spinNumPaxindex);
            spinPax.setEnabled(false);
            String spot_name;

            for (int x = 0; x < customData.getPackageItinerary().size(); x++) {
                packItinerary.add(customData.getPackageItinerary().get(x).getStartTime()+"\t\t\t\t "+customData.getPackageItinerary().get(x).getEndTime()+"\t\t\t\t "+customData.getPackageItinerary().get(x).getSpotID()+"\t\t\t\t");

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, packItinerary);
            mListViewItinerary = (ListView) findViewById(R.id.CustomPackageItineraryListView);
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
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.setBackgroundColor(Color.GREEN);
                addEvent();

            }
        });
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edtDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CustomPackageActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtDate.setText(sdf.format(myCalendar.getTime()));
        eventDate = String.valueOf(edtDate.getText());
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(final MenuItem item) {
    Controllers con = new Controllers();
        ArrayList<CustomizedPackage> custList = new ArrayList<>();
        custList = con.getCustomizedPackageList();
        switch (item.getItemId()) {
            case R.id.save:
                if(!Objects.equals(type, "create")){
                    con.removeCustomizedPackage(Integer.parseInt(pos));
                   ArrayList<Itinerary> temp = new ArrayList<>();
                    temp = con.getCustomizedPackageList().get(Integer.parseInt(pos)).getPackageItinerary();

                    int hours = Integer.parseInt(txtDays.getText().toString()) * 8;
                    ArrayList<Spots> spotListemp = new ArrayList<>();
                    spotListemp = con.getControllerSpots();
                    double distance = 0;

                    String spotIdStart, spotIdEnd;

                    LatLng startp = null;
                    LatLng endp = null;
                    double distanceprice, finalPrice;
                    for (int z = 1; z < temp.size(); z++) {
                        int x = z + 1;
                        spotIdStart = temp.get(z).getSpotID();
                        spotIdEnd = temp.get(x).getSpotID();


                        if (z == 0) {
                            startp = con.getCurrentLocation();
                            if(startp.equals(null)){
                                startp = curlocation;
                            }
                            endp = new LatLng(Double.parseDouble(spotListemp.get(0).getSpotLocationLat()),Double.parseDouble(spotListemp.get(0).getSpotLocationLong()));
                        } else {
                            startp = null;
                            endp = null;
                            for(int q = 0; q < spotListemp.size();q++) {
                                if (spotListemp.get(q).getSpotID().equals(spotIdStart)) {

                                    startp = new LatLng(Double.parseDouble(spotListemp.get(q).getSpotLocationLat()), Double.parseDouble(spotListemp.get(q).getSpotLocationLong()));

                                }
                                if (spotListemp.get(q).getSpotID().equals(spotIdEnd)) {

                                    endp = new LatLng(Double.parseDouble(spotListemp.get(q).getSpotLocationLat()), Double.parseDouble(spotListemp.get(q).getSpotLocationLong()));

                                }
                            }


                        }
                        if (startp != null && endp != null) {
                            distance = con.CalculationByDistance(startp, endp);

                            Log.d("CustomPackage", "Distance :" + distance);
                        } else {
                            Log.d("CustomPackage", "Distance is null");
                        }


                    }
                    distanceprice = distance * 50;
                    finalPrice = (hours * distanceprice) * Double.parseDouble(spinPaxnum);

                    Log.d("CustomPackage", "Distance price :" + distanceprice);
                    Log.d("CustomPackage", "Final price :" + finalPrice);
                    CustomizedPackage cust = new CustomizedPackage(txtPackName.getText().toString(), spinLanguage, spinTGnum, spinPaxnum, String.valueOf(finalPrice), temp, Integer.parseInt(txtDays.getText().toString()), R.mipmap.ic_tourista, edtDescription.getText().toString(), eventDate);
                    con.addCustomizedPackage(cust);


                    Intent i = new Intent(CustomPackageActivity.this, TourActivity.class);
                    startActivity(i);

                }
                else {

                    ArrayList<Itinerary> tempo = new ArrayList<>();
                    CustomizedPackage cust = new CustomizedPackage(txtPackName.getText().toString(), spinLanguage, spinTGnum, spinPaxnum, "0", tempo, Integer.parseInt(txtDays.getText().toString()), R.mipmap.ic_tourista, edtDescription.getText().toString(), eventDate);
                    con.addCustomizedPackage(cust);
                    Intent i = new Intent(CustomPackageActivity.this, TourActivity.class);
                    startActivity(i);
                }
                return true;
            case R.id.edit:
                btnBook.setVisibility(View.VISIBLE);
                edtDate.setFocusable(true);
                edtDescription.setFocusable(true);
                spinLang.setEnabled(true);
                spinNumTG.setEnabled(true);
                spinPax.setEnabled(true);
                txtPackName.setFocusable(true);
                txtPackName.setEnabled(true);
                txtPackName.setClickable(true);
                txtPackName.setFocusableInTouchMode(true);
                txtDays.setFocusable(true);
                txtDays.setEnabled(true);
                txtDays.setClickable(true);
                txtDays.setFocusableInTouchMode(true);
                txtPrice.setFocusable(true);
                txtPrice.setEnabled(true);
                txtPrice.setClickable(true);
                txtPrice.setFocusableInTouchMode(true);
                imgV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                });

                return true;
            case R.id.delete:
                if(custList.get(Integer.parseInt(pos))!=null){
                    con.removeCustomizedPackage(Integer.parseInt(pos));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void addEvent() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mFinalCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            edtDate.setText("No network connection available.");
        } else {
            if(customData.getPackageItinerary().size()!=0) {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());
                Controllers con = new Controllers();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = df.format(c.getTime());

                JSONObject obj = new JSONObject();
                try {
                    obj.put("userId", con.getCurrentUserID());
                    obj.put("packageId", "customized");
                    obj.put("reserveDate", formattedDate);
                    obj.put("tourDate", edtDate.getText());
                    obj.put("numOfPeople", spinPaxnum);
                    obj.put("status", "Request");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CustomPackageActivity.POSTRequestPackage pr = new CustomPackageActivity.POSTRequestPackage();
                pr.execute(obj);

            }

        }
    }
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mFinalCredential.setSelectedAccountName(accountName);
                addEvent();
            } else {
                startActivityForResult(
                        mFinalCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    edtDate.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    addEvent();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mFinalCredential.setSelectedAccountName(accountName);
                        addEvent();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    addEvent();
                }
                break;
            case RESULT_LOAD_IMAGE:
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();



                    Bitmap bmp = null;
                    try {
                        bmp = getBitmapFromUri(selectedImage);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    imgV.setImageBitmap(bmp);

                }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                CustomPackageActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        con.setCurrentLocation(new LatLng(location.getLatitude(),location.getLongitude()));
        curlocation = new LatLng(location.getLatitude(),location.getLongitude());
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Java Quickstart")
                    .build();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {

                return getDataFromApi();

            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<String> getDataFromApi() throws IOException {
            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<String>();



            Event event = new Event()

                    .setId(CurrentUser.userFirebaseId+""+edtDate.getText())
                    .setSummary(txtPackName.getText().toString())
                    .setLocation(pack.getSpotItinerary().get(0).getSpotAddress())
                    .setDescription("The day of the tour: "+txtPackName.getText().toString());
            DateTime startDateTime = new DateTime(eventDate+"T"+pack.getPackageItinerary().get(0).getStartTime()+":00-"+pack.getPackageItinerary().get(0).getEndTime());
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("Asia/Dhaka");
            event.setStart(start);
            DateTime endDateTime = new DateTime(eventDate+"T"+pack.getPackageItinerary().get(pack.getPackageItinerary().size()-1).getStartTime()+":00-"+pack.getPackageItinerary().get(pack.getPackageItinerary().size()-1).getEndTime());
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("Asia/Dhaka");
            event.setEnd(end);

            EventAttendee[] attendees = new EventAttendee[]{
                    new EventAttendee().setEmail(CurrentUser.email)

            };
            event.setAttendees(Arrays.asList(attendees));

            EventReminder[] reminderOverrides = new EventReminder[]{
                    new EventReminder().setMethod("email").setMinutes(24 * 60),
                    new EventReminder().setMethod("popup").setMinutes(50),
            };
            Event.Reminders reminders = new Event.Reminders()
                    .setUseDefault(false)
                    .setOverrides(Arrays.asList(reminderOverrides));
            event.setReminders(reminders);

            String calendarId = "primary";
            try {
                event = mService.events().insert(calendarId, event).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            eventStrings.add(""+event);
            return eventStrings;
        }


        @Override
        protected void onPreExecute() {

            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
            } else {
                output.add(0, "Data retrieved using the Google Calendar API:");
                Intent i = new Intent(CustomPackageActivity.this, TourActivity.class);

                startActivity(i);
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();

            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            BooknowActivity.REQUEST_AUTHORIZATION);
                } else {
                }
            } else {
            }

        }}

    public class POSTRequestPackage extends AsyncTask<JSONObject, Void, String> {

        @Override
        protected String doInBackground(JSONObject... params) {
            Controllers con = new Controllers();
            con.postToDb("/api/book-package",params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String rt) {
            CustomPackageActivity.MakeRequestTask mak = new CustomPackageActivity.MakeRequestTask(mFinalCredential);
            mak.execute();
            super.onPostExecute(rt);
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}



