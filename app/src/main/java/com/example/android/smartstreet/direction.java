package com.example.android.smartstreet;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shital on 3/3/16.
 */
public class direction extends AppCompatActivity {

    //to add to the git

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placeslist);

        ArrayList<Bundle> places = getIntent().getParcelableArrayListExtra("key");
        String category = getIntent().getStringExtra("placename");
        ListView listView = (ListView) findViewById(R.id.places_list);
        listView.setAdapter(new SearchAdapter(places));
    }

    class SearchAdapter extends BaseAdapter {
        String placeName = "";
        String placeVicinity = "";
        String category = "";
        private ArrayList<Bundle> placeList;

        public SearchAdapter(ArrayList<Bundle> placeList) {
            this.placeList = placeList;
        }

        @Override
        public int getCount() {
            return placeList.size();
        }

        @Override
        public Object getItem(int position) {
            return placeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //getting the place array from result
            Bundle placeDetail = placeList.get(position);
            Bundle name = placeDetail.getBundle("name");
            assert name != null;
            //getting place name
            placeName = name.getString("name");

            Bundle vicinity = placeDetail.getBundle("vicinity");
            assert vicinity != null;
            //getting place address
            placeVicinity = vicinity.getString("vicinity");

            Bundle placeLat = placeDetail.getBundle("lat");
            assert placeLat != null;
            Double currentLat = placeLat.getDouble("lat");

            Bundle placeLng = placeDetail.getBundle("lng");
            assert placeLng != null;
            Double currentLng = placeLng.getDouble("lng");
            //
            LayoutInflater inflater = getLayoutInflater();
            View listView;
            listView = inflater.inflate(R.layout.steps, parent, false);
            TextView locationAddress, locationName, nearbyplacesName;

            //setting the time, distance and instructions in layout
            locationName = (TextView) listView.findViewById(R.id._instruction);
            locationAddress = (TextView) listView.findViewById(R.id._distance);
            nearbyplacesName = (TextView) findViewById(R.id.placesname);
            locationName.setText(placeName);
            locationAddress.setText(placeVicinity);
            nearbyplacesName.setText("Nearby Places");
            return (listView);
        }
    }
}


