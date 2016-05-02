package com.example.android.smartstreet;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
public class Steps extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.directions);
        ArrayList<Bundle> direction = getIntent().getParcelableArrayListExtra("step");
        String completeDistanceText = getIntent().getStringExtra("distance");
        String completeDurationText = getIntent().getStringExtra("duration");


        ListView listView = (ListView) findViewById(R.id.directionlist);
        listView.setAdapter(new StepsAdapter(direction));
    }

    //to add to the git

    /**
     * Custom BaseAdapter for the stepsdetail array
     */
    class StepsAdapter extends BaseAdapter {
        private ArrayList<Bundle> stepDetails;

        public StepsAdapter(ArrayList<Bundle> stepDetails) {
            this.stepDetails = stepDetails;
        }

        @Override
        public int getCount() {
            return stepDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return stepDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        //Displayong the direction details on the listview
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //getting distance from stepdetail bundle
            Bundle stepDetail = stepDetails.get(position);
            Bundle distanceBundle = stepDetail.getBundle("distance");
            assert distanceBundle != null;
            String distance = distanceBundle.getString("text");
            //getting duration from stepdetail bundle
            Bundle durationBundle = stepDetail.getBundle("duration");
            assert durationBundle != null;
            String duration = durationBundle.getString("text");

            //getting html dinstructions from stepdetail bundle
            Bundle instructionBundle = stepDetail.getBundle("instruction");
            assert instructionBundle != null;
            String instruction = instructionBundle.getString("value");

            LayoutInflater inflater = getLayoutInflater();
            View listView;
            listView = inflater.inflate(R.layout.steps, parent, false);
            TextView durationText, distanceText, instructionText;
            //setting the time, distance and instructions in layout
            durationText = (TextView) listView.findViewById(R.id._time);
            distanceText = (TextView) listView.findViewById(R.id._distance);
            instructionText = (TextView) listView.findViewById(R.id._instruction);

            distanceText.setText(distance + " : ");
            durationText.setText(duration);
            instructionText.setText(instruction);

            return (listView);
        }
    }
}

