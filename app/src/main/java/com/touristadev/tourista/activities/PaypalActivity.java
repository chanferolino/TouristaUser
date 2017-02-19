package com.touristadev.tourista.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.touristadev.tourista.R;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.BookedPackages;
import com.touristadev.tourista.dataModels.TouristaPackages;
import com.touristadev.tourista.models.CurrentUser;
import com.touristadev.tourista.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PaypalActivity extends AppCompatActivity {

    private Button btnPaypal;

    private Calendar myCalendar;
    private EditText edtDate;
    private ArrayList<TouristaPackages> mList = new ArrayList<>();
    private int position;
    private String typePackage, packageTitle;
    private int flag = 0;
    private String paxData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);
        btnPaypal = (Button) findViewById(R.id.btnPayPal);
        edtDate = (EditText) findViewById(R.id.edtDate);

        myCalendar = Calendar.getInstance();
        Intent i = getIntent();
        mList = Controllers.getControllerPackaaes();
        position = i.getIntExtra("position", 0);
        typePackage = i.getStringExtra("type");
        packageTitle = i.getStringExtra("title");
        paxData = i.getStringExtra("pax");
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                flag = 1;
                btnPaypal.setEnabled(true);
            }

        };

        edtDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PaypalActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Successfully Paid with PayPal!",
                        Toast.LENGTH_LONG).show();
                if(flag == 1){
                    if (typePackage.equals("tour") || typePackage.equals("deal")) {
                        for (int x = 0; x < mList.size(); x++) {
                            if (mList.get(x).getPackageName().equals(packageTitle)) {
//                            Controllers.addWishPackage(mList.get(x));
//                            Controllers.addBookedPackages(new BookedPackages(mList.get(x).getPackageId(),mList.get(x).getPackageName(),mList.get(x).getPackageItinerary(),mList.get(x).getPackageTourGuideClassification(),mList.get(x).getRating(),mList.get(x).getPackageNoOfSpots(),mList.get(x).getPackageTotalNoOfHours(),mList.get(x).getPackageImage(),mList.get(x).getSpotItinerary(),mList.get(x).getPackDescription(),mList.get(x).getPackPrice(),edtDate.getText().toString(),mList.get(x).getCompanyName()));
                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("to", "/topics/news");
                                    JSONObject data = new JSONObject();
                                    data.put("nameOfTourist", CurrentUser.name);
                                    data.put("numberOfPerson", paxData);
                                    data.put("userId", CurrentUser.userFirebaseId);
                                    data.put("packageId", mList.get(x).getPackageId());
                                    data.put("reserveDate", edtDate.getText().toString());
                                    data.put("paymentforTG", mList.get(x).getPackPrice());
                                    data.put("notifType", "Request");
                                    jsonObject.put("data", data);

                                    JSONObject notification = new JSONObject();
                                    notification.put("title", "Incoming Request..");
                                    notification.put("body", mList.get(x).getPackageName());
                                    jsonObject.put("notification", notification);

                                    new PaypalActivity.RequestTask().execute(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(getApplicationContext(), "Added " + mList.get(x).getPackageName() + " to Wish List",
                                        Toast.LENGTH_LONG).show();


                            }
                        }
                    }
                }}
        });
    }
    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edtDate.setText(sdf.format(myCalendar.getTime()));
    }
    public class RequestTask extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {
            HttpUtils.POST("https://fcm.googleapis.com/fcm/send", jsonObjects[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent(PaypalActivity.this, TourActivity.class);
            startActivity(i);

        }
    }
}