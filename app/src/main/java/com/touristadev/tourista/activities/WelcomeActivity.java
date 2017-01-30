package com.touristadev.tourista.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.touristadev.tourista.R;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mWelcome, txtWelcomeText, tvWelcome;
    private Button mProceed, btnProceed;
    private String firstName,lastName, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_welcome);



        mWelcome = (TextView)findViewById(R.id.tvWelcome);
        Intent i = getIntent();
        mWelcome.setText(i.getStringExtra("firstName"));

        firstName = i.getStringExtra("firstName");
        lastName = i.getStringExtra("lastName");
        email = i.getStringExtra("email");
        mProceed = (Button)findViewById(R.id.btnProceed);
        mProceed.setOnClickListener(this);

        //FONTS
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Bold.ttf");

        btnProceed= (Button) findViewById(R.id.btnProceed) ;
        btnProceed.setTypeface(myCustomFont);

        txtWelcomeText= (TextView) findViewById(R.id.txtWelcomeText) ;
        txtWelcomeText.setTypeface(myCustomFont);

        tvWelcome= (TextView) findViewById(R.id.tvWelcome) ;
        tvWelcome.setTypeface(myCustomFont);


    }

    @Override
    public void onClick(View v) {
        //diri mo proceed para view pager
        Intent intent = new Intent(WelcomeActivity.this, TutorialActivity.class);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}
