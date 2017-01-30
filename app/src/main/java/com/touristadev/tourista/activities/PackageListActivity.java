package com.touristadev.tourista.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.touristadev.tourista.R;
import com.touristadev.tourista.fragments.CityToursFragment;
import com.touristadev.tourista.fragments.HotToursFragment;
import com.touristadev.tourista.fragments.PassportFragment;

public class PackageListActivity extends AppCompatActivity {
    private String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);
        Fragment fragment = null;
        Intent i = getIntent();
        city = i.getStringExtra("City");
        if(city.equals("Cebu City")) {
            fragment = new CityToursFragment();
        }
        else{
            fragment = new HotToursFragment();
        }
        if (fragment != null) {

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment).commit();
        }
    }
}
