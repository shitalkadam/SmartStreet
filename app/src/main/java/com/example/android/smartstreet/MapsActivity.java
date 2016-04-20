package com.example.android.smartstreet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment supportMapFragment;
    Button search_bt;
    EditText search_text;
    String search_category;
    ListView listView;
    ImageButton logoButton;
    double destinationLat;
    double destinationLng;
    ArrayList<Bundle> placesResult;
    ArrayList<Bundle> stepDetails;
    String completeDistanceText;
    String completeDurationText;
    Button list_button;
    private GoogleMap myMap;
    private double myLocationLatitude;
    private double myLocationLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //This will notify when map is ready by using SupportMapFragment
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        //Custom Actionbar will be displayed by calling customActionBar() method
        customActionBar();

        search_bt = (Button) findViewById(R.id.search_button);
        search_text = (EditText) findViewById(R.id.search_category);
        search_text.getBackground().clearColorFilter();

        list_button = (Button) findViewById(R.id.list_button);
        list_button.setClickable(false);

    }

    /**
     * This function creates the custom toolbar for the application
     * Including the application icon and application name and a refresh button
     */
    public void customActionBar() {
        android.support.v7.app.ActionBar myActionBar = getSupportActionBar();
        if (myActionBar != null) {
            myActionBar.setDisplayShowHomeEnabled(false);
        }
        if (myActionBar != null) {
            myActionBar.setDisplayShowTitleEnabled(false);
        }
        //layout inflater will inflate the custom_actionbar layout in actionbar
        LayoutInflater MyLayout = LayoutInflater.from(this);
        View View = MyLayout.inflate(R.layout.custom_actionbar, null);

        //Toast will appear on clicking logobutton

        if (myActionBar != null) {
            myActionBar.setCustomView(View);
        }
        if (myActionBar != null) {
            myActionBar.setDisplayShowCustomEnabled(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about, menu);
        return true;
    }

    //onMapReady user's location will be displayed
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myMap.setMyLocationEnabled(true);
        try {
            getMyLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method gives the present location of user
     * and also adds the marker on that location
     * On clicking the marker "You are here" is displayed
     */
    public void getMyLocation() throws IOException {
        LocationManager myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria placeCriteria = new Criteria();
        String placeprovider = myLocationManager.getBestProvider(placeCriteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location myPresentLocation = myLocationManager.getLastKnownLocation(placeprovider);
        double myLatitude = myPresentLocation.getLatitude();
        double myLongitude = myPresentLocation.getLongitude();
        this.myLocationLatitude = myLatitude;
        this.myLocationLongitude = myLongitude;

        //Camera will move to user's location with zoom 12
        CameraUpdate myLocation = CameraUpdateFactory.newLatLng(new LatLng(myLatitude, myLongitude));
        myMap.moveCamera(myLocation);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
        myMap.animateCamera(zoom);

        //Reserver Geocoder is used to get the address of the user's location
        Geocoder myGeocoder;
        List<Address> myAddresses;
        myGeocoder = new Geocoder(this, Locale.getDefault());

        myAddresses = myGeocoder.getFromLocation(myLocationLatitude, myLocationLongitude, 1);
        String myAddress = myAddresses.get(0).getAddressLine(0);
        String myCity = myAddresses.get(0).getLocality();
        String myState = myAddresses.get(0).getAdminArea();
        String zipCode = myAddresses.get(0).getPostalCode();
        //Adding marker on the map at user's location
        Marker myMarker = myMap.addMarker(new MarkerOptions()
                .position(new LatLng(myLatitude, myLongitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title(myAddress)
                .snippet(myCity + "-" + zipCode + " , " + myState));
        PlaceInfoWindow placeInfoWindow = new PlaceInfoWindow(getLayoutInflater());
        myMap.setInfoWindowAdapter(placeInfoWindow);
        myMarker.showInfoWindow();
    }

    /**
     * this will invoke when search button is clicked
     * placesTask is AsyncTask which is running in background and downloads the places
     */

    public void nearBySearch(View view) throws IOException {

        StringBuilder placeStringBuilder = new StringBuilder(placeStringBuilder());
        NearByPlacesTask placesTask = new NearByPlacesTask();
        placesTask.execute(placeStringBuilder.toString());
        list_button.setClickable(true);
    }

    /**
     * StringBuilder builds the search-string using the dynamic input from user
     * String contains the URL for the web server with the query parameter and the server API key
     *
     * @return searchString
     */
    public StringBuilder placeStringBuilder() {
        search_category = search_text.getText().toString().toLowerCase();
        String query = "";
        try {
            query = URLEncoder.encode(search_category, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder searchString = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        searchString.append("location=").append(myLocationLatitude).append(",").append(myLocationLongitude);
        searchString.append("&radius=12000");
        searchString.append("&types=").append(query);
        searchString.append("&sensor=true");
        searchString.append("&key=AIzaSyBJVnTyZ1ZLBB7ouGtokakAmV_jFfKsGrk");
        return searchString;
    }

    /**
     * This method reads the near by places data from the passed URL
     * and saves the data in urlData string
     * returns urlData
     */
    private String downloadPlacesUrl(String stringURL) throws IOException {
        HttpURLConnection newhttpURLConn = null;
        InputStream placesStream = null;

        String urlData = "";
        try {
            URL placesDataUrl = new URL(stringURL);
            //creating a new http connection
            newhttpURLConn = (HttpURLConnection) placesDataUrl.openConnection();

            //connecting to new newhttpURLConn
            newhttpURLConn.connect();

            //Reading near by placesData
            placesStream = newhttpURLConn.getInputStream();

            BufferedReader stringReader = new BufferedReader(new InputStreamReader(placesStream));

            StringBuffer stringData = new StringBuffer();
            //downloading data and storing in String
            String eachLine = "";
            while ((eachLine = stringReader.readLine()) != null) {
                stringData.append(eachLine);
            }
            urlData = stringData.toString();
            //closing the connection
            stringReader.close();

        } catch (Exception e) {
        e.printStackTrace();
        } finally {

            if (placesStream != null) {
                placesStream.close();
            }
            if (newhttpURLConn != null) {
                newhttpURLConn.disconnect();
            }
        }
        return urlData;
    }
    //method will invoke on list button click
    public void getList(View v) {
       //creating new intent which starts new activity
        Intent intent = new Intent(this, direction.class);
        intent.putParcelableArrayListExtra("key", placesResult);
        intent.putExtra("placename", search_category);
        startActivityForResult(intent, 100);
    }
    // methos will invoke the directionTask
    public void directions(View view) {

        StringBuilder directionBuilder = new StringBuilder(directionBuilder());
        DirectionTask directionDetailTask = new DirectionTask();
        directionDetailTask.execute(directionBuilder.toString());
    }
    // building the string to query the directions
    public StringBuilder directionBuilder() {
        StringBuilder search_query = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        search_query.append("origin=").append(myLocationLatitude).append(",").append(myLocationLongitude);
        search_query.append("&destination=").append(destinationLat).append(",").append(destinationLng);
        search_query.append("&key=AIzaSyBJVnTyZ1ZLBB7ouGtokakAmV_jFfKsGrk");
        return search_query;
    }

    /**
     * This AsyncTask will download the places data on background thread and saves data in placesData string
     * It will start parsing places data in Json object
     */
    private class NearByPlacesTask extends AsyncTask<String, Integer, String> {

        String placesData = null;
        //On execute() this method is invoked
        @Override
        protected String doInBackground(String... placeUrl) {
            try {
                placesData = downloadPlacesUrl(placeUrl[0]);
            } catch (Exception de) {
                de.printStackTrace();

            }
            return placesData;
        }

        //After the completion of "doInBackground()"this method will be executed
        //placesdata is passed in this method
        @Override
        protected void onPostExecute(String result) {
            myMap.clear();
            try {
                getMyLocation();
            } catch (IOException e) {
                e.printStackTrace();
            }
            placesResult = new ArrayList<>();
            //parsing the JSON data and collecting it in the arraylist
            try {
                JSONObject directionJson = new JSONObject(result);
                JSONArray resultPlaces = directionJson.getJSONArray("results");
                //looping through the places array
                for (int i = 0; i < resultPlaces.length(); i++) {
                    JSONObject singlePlace = resultPlaces.getJSONObject(i);
                    //getting the place name
                    String placeName = singlePlace.getString("name");
                    Bundle name = new Bundle();
                    name.putString("name", placeName);
                    //getting the place address
                    String placeVicinity = singlePlace.getString("vicinity");
                    Bundle vicinity = new Bundle();
                    vicinity.putString("vicinity", placeVicinity);

                    JSONObject geometry = singlePlace.getJSONObject("geometry");
                    JSONObject placeLocation = geometry.getJSONObject("location");
                    //getting place latitude
                    double placeLat = Double.parseDouble(placeLocation.getString("lat"));
                    Bundle positionLat = new Bundle();
                    positionLat.putDouble("positionLat", placeLat);
                    //getting place longitude
                    double placeLng = Double.parseDouble(placeLocation.getString("lng"));
                    Bundle positionLng = new Bundle();
                    positionLng.putDouble("positionLng", placeLng);

                    Bundle data = new Bundle();
                    data.putBundle("name", name);
                    data.putBundle("vicinity", vicinity);
                    data.putBundle("lat", positionLat);
                    data.putBundle("lng", positionLng);

                    placesResult.add(data);
                    //creating the marker options
                    MarkerOptions placeMarkerOptions = new MarkerOptions();

                    placeMarkerOptions.position(new LatLng(placeLat, placeLng));
                    placeMarkerOptions.title(placeName);
                    placeMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    placeMarkerOptions.snippet(placeVicinity + " " + "Contact:(501)6546");
                    //Placing the marker on Map
                    final Marker placeMarker = myMap.addMarker(placeMarkerOptions);

                    //On marker touch showing the infoWindow
                    PlaceInfoWindow placeWindow = new PlaceInfoWindow(getLayoutInflater());
                    myMap.setInfoWindowAdapter(placeWindow);
                    placeMarker.showInfoWindow();
                    //infowindow click method invoke
                    myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            destinationLat = placeMarker.getPosition().latitude;
                            destinationLng = placeMarker.getPosition().longitude;

                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creating the custom PlaceInfoWindow for the Place Info to display
     */
    class PlaceInfoWindow implements GoogleMap.InfoWindowAdapter {
        LayoutInflater inflater = null;

        PlaceInfoWindow(LayoutInflater infoinflater) {
            this.inflater = infoinflater;
        }

        @Override
        public View getInfoWindow(Marker myMarker) {
            return null;
        }

        /**
         * Setting the Information on popup layout
         * Returns place information
         */
        @Override
        public View getInfoContents(Marker placeMarker) {
            View placeInfo = inflater.inflate(R.layout.popup, null);
            TextView place = (TextView) placeInfo.findViewById(R.id.title);
            place.setText(placeMarker.getTitle());

            place = (TextView) placeInfo.findViewById(R.id.place_snippet);
            place.setText(placeMarker.getSnippet());

            return (placeInfo);
        }
    }

    private class DirectionTask extends AsyncTask<String, Integer, String> {
        String directiondata = null;

        @Override
        protected String doInBackground(String... url) {
            try {
                directiondata = downloadPlacesUrl(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return directiondata;
        }

        /**
         * result is directions details data
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            ArrayList<Bundle> stepDetails = new ArrayList<>();
            completeDistanceText = null;
            completeDurationText = null;

            try {
                //getting the result json
                JSONObject placeDirectionJson = new JSONObject(result);
                //getting routs output as array
                JSONArray placeRoutes = placeDirectionJson.getJSONArray("routes");
                //getting the first route
                JSONObject firstRoute = (JSONObject) placeRoutes.get(0);
                //getting array of legs in first route
                JSONArray routeLegs = firstRoute.getJSONArray("legs");
                //getting the first leg in first route
                JSONObject routeFirstLeg = (JSONObject) routeLegs.get(0);

                //getting the complete distance from source to destination
                JSONObject completeDistance = routeFirstLeg.getJSONObject("distance");
                completeDistanceText = completeDistance.getString("text");

                //getting complete duration from source to destination
                JSONObject completeDuration = routeFirstLeg.getJSONObject("duration");
                completeDurationText = completeDuration.getString("text");

                /*looping through legs array in first route and adding all waypoint duration, distance, instructions in array of Bundle*/
                JSONArray directionSteps = routeFirstLeg.getJSONArray("steps");
                for (int i = 0; i < directionSteps.length(); i++) {
                    JSONObject singleRouteStep = directionSteps.getJSONObject(i);
                    JSONObject durationJsonObject = singleRouteStep.getJSONObject("duration");
                    Bundle durtionBundle = new Bundle();
                    durtionBundle.putString("text", durationJsonObject.getString("text"));
                    durtionBundle.putString("value", durationJsonObject.getString("value"));
                    JSONObject distanceJson = singleRouteStep.getJSONObject("distance");
                    Bundle distanceBundle = new Bundle();
                    distanceBundle.putString("text", distanceJson.getString("text"));
                    distanceBundle.putString("value", distanceJson.getString("value"));
                    String instruction = singleRouteStep.getString("html_instructions");
                    Bundle instr = new Bundle();
                    instr.putString("text", "next");
                    instr.putString("value", Html.fromHtml(instruction).toString());
                    Bundle directionData = new Bundle();
                    directionData.putBundle("duration", durtionBundle);
                    directionData.putBundle("distance", distanceBundle);
                    directionData.putBundle("instruction", instr);
                    stepDetails.add(directionData);
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }

            //setting the layout to display the directions in listview
            setContentView(R.layout.directions);

            TextView completeDistance = (TextView) findViewById(R.id.completedistance);
            TextView completeDuration = (TextView) findViewById(R.id.completeduration);
            completeDistance.setText(completeDistanceText);

            completeDuration.setText("(" + completeDurationText + ")");

            listView = (ListView) findViewById(R.id.directionlist);
            listView.setAdapter(new StepsAdapter(stepDetails));
        }

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
                String PlaceDistance = distanceBundle.getString("text");
                //getting duration from stepdetail bundle
                Bundle durationBundle = stepDetail.getBundle("duration");
                assert durationBundle != null;
                String placeDuration = durationBundle.getString("text");

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

                distanceText.setText(PlaceDistance + " : ");
                durationText.setText(placeDuration);
                instructionText.setText(instruction);

                return (listView);
            }
        }
    }

}