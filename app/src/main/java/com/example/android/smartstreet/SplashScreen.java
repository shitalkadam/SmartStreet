package com.example.android.smartstreet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shital on 3/1/16.
 */
public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent splash_intent = new Intent(this, UserLogin.class);
        startActivity(splash_intent);
        finish();
    }

    //to add to the git


}
