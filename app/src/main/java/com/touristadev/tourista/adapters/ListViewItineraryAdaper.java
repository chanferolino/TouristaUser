package com.touristadev.tourista.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.touristadev.tourista.R;
import com.touristadev.tourista.activities.CustomPackageActivity;
import com.touristadev.tourista.controllers.Controllers;
import com.touristadev.tourista.dataModels.listViewPackItinerary;

import java.util.List;

/**
 * Created by Christian on 2/20/2017.
 */
public class ListViewItineraryAdaper extends ArrayAdapter<listViewPackItinerary> {

    private int layoutResource;
    Context con;
    public ListViewItineraryAdaper(Context context, int layoutResource, List<listViewPackItinerary> threeStringsList) {
        super(context, layoutResource, threeStringsList);
        con = context;
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        listViewPackItinerary threeStrings = getItem(position);

        if (threeStrings != null) {
            TextView txtName = (TextView) view.findViewById(R.id.txtName);
            ImageView imgCancel = (ImageView) view.findViewById(R.id.imgCancel);

            if (txtName != null) {
                txtName.setText(threeStrings.getName());
            }

                imgCancel.setImageResource(R.drawable.ic_delete);
                imgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Controllers.removeSpotToPackage(pos);
                        notifyDataSetChanged();
                    }
                });


        }

        return view;
    }
}