package com.example.android.smartstreet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class UserLogin extends AppCompatActivity {
    ImageButton logoButton;

    private static final String URL = "https://smartstreetapp.firebaseio.com";
    Firebase ref;

    String passDb;
    String firstNameDb="";
    String emailDb;

    EditText emailText;
    EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_user_login);
        customActionBar();
        emailText = (EditText) findViewById(R.id.email_text);
        passwordText = (EditText) findViewById(R.id.password_text);
    }

    //to add to the git
    //creating actionbar
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
    //method will invoke when user clicks on login button
    public void submit(View view){
        // Create a handler to handle the result of the authentication
        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // Authenticated successfully with payload authData

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Authenticated failed with error firebaseError
            }
        };


        //getting user name from view
        String userName = emailText.getText().toString();
        //getting password from view
        String password = passwordText.getText().toString();
        ref = new Firebase(URL);

        ref.authWithPassword(userName,password,authResultHandler);

        AuthData authData = ref.getAuth();
        if (authData != null){
               //if username and password matches launches the home page
                Intent home_intent = new Intent(this,MainActivity.class);
                //putting user's name with hame page intent
                home_intent.putExtra("firstname", userName);
                startActivity(home_intent);
                this.finish();
            }
    }
    //launches the registration activity
    public void register(View view){
        Intent register_intent = new Intent(this,Registration.class);
        startActivity(register_intent);
        this.finish();
    }
    //launches the home activity
    public void home(View view){
        Intent home_intent = new Intent(this,MainActivity.class);
        startActivity(home_intent);
        this.finish();
    }

}
