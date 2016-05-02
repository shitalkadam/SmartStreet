package com.example.android.smartstreet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MusicInteract extends AppCompatActivity {

    PubNubHelper helper = new PubNubHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_interact);
        helper = new PubNubHelper();


    }

    //to add to the git

    public void play(View view){
        helper.publishPlayMusic();
    }

    public void pause(View view){
        helper.publishPauseMusic();
    }

    public void stop(View view){
        helper.publishStopMusic();
    }

    public void next(View view){
        helper.publishNextMusic();
    }

    public void previous(View view){
        helper.publishPreviousMusic();
    }
}
