package com.touristadev.tourista.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.touristadev.tourista.R;

public class PolicyActivity extends AppCompatActivity {
    private TextView cancPol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        cancPol = (TextView) findViewById(R.id.textView4);
        cancPol.setText("Cancellation agreement\n" +
                "\n" +
                "Cancellation of tours may only be upon circumstances when:\n" +
                "a.) Client cancels the tour. If client cancels the tour for their unavailability, the client is charged _______ for cancellation fee.\n" +
                "\n" +
                "b.) nature does not permits. Unforeseen circumstances like, typhoon, earthquakes and other disasters, the tour will then be automatically be moved to other dates with the clients notice.\n" +
                "\n" +
                "Cancellation of tour, may only be done 5-7 days before the scheduled date.");
    }
}
