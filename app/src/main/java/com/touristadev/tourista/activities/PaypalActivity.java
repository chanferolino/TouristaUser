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
import com.touristadev.tourista.dataModels.TouristaPackages;
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
    private Controllers mControllers = new Controllers();
    private ArrayList<TouristaPackages> mList = new ArrayList<>();
    private int position;
    private String typePackage, packageTitle;
    private int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);
        btnPaypal = (Button) findViewById(R.id.btnPayPal);
        edtDate = (EditText) findViewById(R.id.edtDate);

        myCalendar = Calendar.getInstance();
        Intent i = getIntent();
        mList = mControllers.getControllerPackaaes();
        position = i.getIntExtra("position", 0);
        typePackage = i.getStringExtra("type");
        packageTitle = i.getStringExtra("title");
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
//                            mControllers.addWishPackages(mList.get(x));
//                            mControllers.addBookedPackages(mList.get(x));
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("to", "/topics/news");
                                JSONObject data = new JSONObject();
                                data.put("message", mList.get(x).getPackageName());
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

                            Intent i = new Intent(PaypalActivity.this, TourActivity.class);
                            startActivity(i);

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
    }
}
