package com.example.android.smartstreet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
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
}
