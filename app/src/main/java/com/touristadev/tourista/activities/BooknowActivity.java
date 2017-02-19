package com.touristadev.tourista.activities;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.common.io.BaseEncoding;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.touristadev.tourista.R;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.TouristaPackages;
import com.touristadev.tourista.models.CurrentUser;
import com.touristadev.tourista.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class BooknowActivity extends AppCompatActivity  implements EasyPermissions.PermissionCallbacks {
    private EditText edtDate;
    private MaterialSpinner spinner,spinLang;
    private Controllers mControllers = new Controllers();
    private ArrayList<TouristaPackages> mList = new ArrayList<>();
    private int position;
    private String typePackage, packageTitle;
    private Button btnCheck;

    private String startTime,endTime;
    private String spinValue;

    private Calendar myCalendar;
    GoogleAccountCredential mFinalCredential;
    ProgressDialog mProgress;

    private TouristaPackages pack = new TouristaPackages();
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR };
    private String eventDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booknow);
        Intent i = getIntent();
        mList = mControllers.getControllerPackaaes();
        position = i.getIntExtra("position", 0);
        typePackage = i.getStringExtra("type");
        packageTitle = i.getStringExtra("title");
        edtDate = (EditText) findViewById(R.id.edtDate);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage(" Adding event to google calendar");

        // Initialize credentials and service object.
        mFinalCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList("https://www.googleapis.com/auth/calendar"))
                .setBackOff(new ExponentialBackOff());
Log.d("LoginAct",mFinalCredential+"");


        btnCheck = (Button) findViewById(R.id.btnCheckout);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BookNowAct","pack1 "+packageTitle);

                for(int x = 0 ; x < mList.size(); x++){
                    if(mList.get(x).getPackageName().equals(packageTitle)){
                        startTime = mList.get(x).getPackageItinerary().get(0).getStartTime();
                        endTime = mList.get(x).getPackageItinerary().get(0).getEndTime();
                        pack = mList.get(x);

                        Log.d("BookNowAct","pack "+pack.getPackageName());

                        addEvent();
                    }
                }

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
                new DatePickerDialog(BooknowActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        spinner = (MaterialSpinner) findViewById(R.id.spnTourGuides);

        spinner.setItems("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                spinValue = item;

                Log.d("BookNowAct","item "+item);
            }
        });
        spinLang = (MaterialSpinner) findViewById(R.id.spinLang);

        spinLang.setItems("English", "Cebuano", "Tagalog", "Japanese", "Chinese", "Korean", "Bicolano", "Waray");
        spinLang.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

            }
        });
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        btnCheck.setEnabled(true);
        edtDate.setText(sdf.format(myCalendar.getTime()));
        eventDate = String.valueOf(edtDate.getText());
    }
    private void addEvent() {
        Log.d("Booknow!",mFinalCredential.getSelectedAccountName()+"");
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mFinalCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
           edtDate.setText("No network connection available.");
        } else {
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
            String formattedDate = df.format(c.getTime());
            Log.d("BookNowAct","1");
            JSONObject obj = new JSONObject();

            Log.d("BookNowAct","Spin "+spinValue);
            try {
                obj.put("userId",Controllers.getCurrentUserID());
                obj.put("packageId",pack.getPackageId());
                obj.put("reserveDate",formattedDate);
                obj.put("tourDate",edtDate.getText());
                obj.put("numOfPeople",spinValue+"");
                obj.put("status","request");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            POSTRequestPackage pr= new POSTRequestPackage();
            pr.execute(obj);





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
                BooknowActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
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

                    Log.d("BookNowAct","3 "+e);
                    cancel(true);
                    return null;
                }
            }

            private List<String> getDataFromApi() throws IOException {
                // List the next 10 events from the primary calendar.
                DateTime now = new DateTime(System.currentTimeMillis());
                List<String> eventStrings = new ArrayList<String>();


                Event event = new Event()
                        .setSummary(packageTitle)
                        .setLocation("Cebu City")
                        .setDescription("The day of the tour: "+packageTitle);

                Log.d("BookNowAct","2a");
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

                Log.d("BookNowAct","2b");
                EventAttendee[] attendees = new EventAttendee[]{
                        new EventAttendee().setEmail(CurrentUser.email)

                };
                Log.d("BookNowAct","2b "+CurrentUser.email);
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

                    Log.d("BookNowAct","Event "+event );
                    event = mService.events().insert(calendarId, event).execute();

                    Log.d("BookNowAct","Event "+event );
                } catch (IOException e) {
                    e.printStackTrace();

                    Log.d("BookNowAct","Exception 2"+e);
                }
               eventStrings.add(""+event);
                return eventStrings;
            }


        @Override
        protected void onPreExecute() {

            Log.d("BookNowAct","4");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
            } else {
                output.add(0, "Data retrieved using the Google Calendar API:");

                Log.d("BookNowAct","5");
                Intent i = new Intent(BooknowActivity.this, PaypalActivity.class);
            i.putExtra("position", position);
            i.putExtra("type", typePackage);
            i.putExtra("pax", spinValue);
            i.putExtra("title", packageTitle);
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
    public class POSTRequestPackage extends AsyncTask<JSONObject, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            Controllers.postToDb("api/book-package",params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject rt) {
            MakeRequestTask mak = new MakeRequestTask(mFinalCredential);
            mak.execute();
            super.onPostExecute(rt);
        }
    }
}





