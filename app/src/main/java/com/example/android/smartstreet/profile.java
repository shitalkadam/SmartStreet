package com.example.android.smartstreet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class profile extends AppCompatActivity {

    private static final String URL = "https://smartstreetapp.firebaseio.com";
    Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        ref = new Firebase(URL);
        AuthData authData = ref.getAuth();
        if(authData != null){
            String user_id = authData.getUid();
            Firebase myFirebaseref = new Firebase(URL+"/users/"+user_id);
            myFirebaseref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String key = snapshot.getKey();
                    UserProfile profile = snapshot.getValue(UserProfile.class);
                    profile.setKey(key);
                    TextView first_name = (TextView) findViewById(R.id.first_name);
                    TextView last_name = (TextView) findViewById(R.id.last_name);
                    TextView email  = (TextView) findViewById(R.id.email_address);
                    TextView contact  = (TextView) findViewById(R.id.contact);
                    first_name.setText(profile.getFirstName());
                    last_name.setText(profile.getLastName());
                    email.setText(profile.getEmail());
                    contact.setText(profile.getPhone());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });
        } else {
            TextView first_name = (TextView) findViewById(R.id.first_name);
            first_name.setText("N/A");
        }
        setContentView(R.layout.activity_profile);
    }

    public void customActionBar() {

        //to add to the git
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
}
