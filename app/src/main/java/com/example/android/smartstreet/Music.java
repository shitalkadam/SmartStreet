package com.example.android.smartstreet;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Music extends AppCompatActivity {
    ImageButton logoButton;
    Handler seekHandler = new Handler();
    String fileType = null;
    String nameOfFile = "smartStreet";
    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    private String musicOutputFile = null;
    private File musicFile;
    private Uri musicUri;
    private ImageButton startButton;
    private ImageButton stopButton;
    private ImageButton playButton;
    private ImageButton stopPlayButton;
    private ImageButton pauseButton;
    private TextView text;
    private Chronometer chronometer;
    private SeekBar seekbar;
    //to add to the git

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        customActionBar();
        //setting the path for music file
        musicOutputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music";
        //music file type
        fileType = ".3gpp";

        File musicDir = null;
        musicDir = new File(musicOutputFile);
        assert musicDir != null;
        if (!musicDir.exists()) {
            musicDir.mkdirs();
        }
        String currentDateAndTime = getCurrentDateAndTime();

        musicFile = new File(musicDir, nameOfFile + currentDateAndTime + fileType);
        //setting music file Uri
        musicUri = Uri.fromFile(musicFile);

        //     text = (TextView) findViewById(R.id.text1);
        startButton = (ImageButton) findViewById(R.id.record_bt);
        stopButton = (ImageButton) findViewById(R.id.stop_bt);

        playButton = (ImageButton) findViewById(R.id.media_play);
        stopPlayButton = (ImageButton) findViewById(R.id.media_stop);
        pauseButton = (ImageButton) findViewById(R.id.media_pause);

        chronometer = (Chronometer) findViewById(R.id.myMeter);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        //creating mediaRecorder instance
        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        //setting the output file
        myRecorder.setOutputFile(String.valueOf(musicFile));
    }

    //Starts the recorder
    public void start(View view) {
        try {
            myRecorder.prepare();
            myRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //setting buttons
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        //starting chornometer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    //stopping the recorder
    public void stop(View view) {
        try {
            myRecorder.stop();
            myRecorder.release();
            myRecorder = null;
            stopButton.setEnabled(false);
//            playButton.setEnabled(true);
            chronometer.stop();
            startButton.setEnabled(true);
            saveFileUsingScanningFunction(musicFile);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {

            e.printStackTrace();
        }
    }
    //getting current date and time to add in file path
    private String getCurrentDateAndTime() {
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return df.format(calender.getTime());
    }
    //playing recorded music
    public void playRecording(View view) {
        try {
            myPlayer = new MediaPlayer();
            myPlayer.setDataSource(String.valueOf(musicFile));
            myPlayer.prepare();
            //setting seekBar length
            seekbar.setMax(myPlayer.getDuration());
            myPlayer.start();
            musicUpdationInSeekbar();
            //           playButton.setEnabled(false);
            //           stopPlayButton.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //pausing the video
    public void pause(View view) {
        try {
            if (myPlayer != null) {
                if (myPlayer.isPlaying()) {
                    myPlayer.pause();
                } else {
                    myPlayer.start();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //stopping the music play
    public void stopPlay(View view) {
        try {
            if (myPlayer != null) {
                myPlayer.stop();
                myPlayer.release();
                myPlayer = null;
                //setting seekbar to zero
                seekbar.setProgress(0);
//                playButton.setEnabled(true);
//                stopPlayButton.setEnabled(false);;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //keeps running the seekbar position
    Runnable seekRun = new Runnable() {
        @Override
        public void run() {
            musicUpdationInSeekbar();
        }
    };
    public void musicUpdationInSeekbar() {
        if (myPlayer != null) {
            seekbar.setProgress(myPlayer.getCurrentPosition());
            seekHandler.postDelayed(seekRun, 1000);
        }
    }
    //saving file on phone storage
    private void saveFileUsingScanningFunction(File file) {
        MediaScannerConnection.scanFile(this,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.e("Storage", "Scanned " + path + ":");
                        Log.e("Storage", "-> musicUri=" + uri);
                    }
                });
    }
    //sharing music file with Gmail and facebook using Chooser
    public void shareMusic(View view) {

        Resources resources = getResources();

        Intent gMailIntent = new Intent();
        gMailIntent.setAction(Intent.ACTION_SEND);
        //setting the email intent extras
        gMailIntent.putExtra(Intent.EXTRA_TEXT, "Sharing Music via Smart Street!");
        gMailIntent.putExtra(Intent.EXTRA_SUBJECT, "Music of Smart Street");
        gMailIntent.putExtra(Intent.EXTRA_STREAM, musicUri);
        gMailIntent.setType("message/rfc822");

        PackageManager packageManager = getPackageManager();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        //creating content of chooser to share music
        Intent chooserOptions = Intent.createChooser(gMailIntent, "Share music with....");
        //getting the list of all available intents
        List<ResolveInfo> infoResolve = packageManager.queryIntentActivities(shareIntent, 0);
        List<LabeledIntent> listOfIntents = new ArrayList<LabeledIntent>();
        //loop through the list of all gathered intents
        for (int i = 0; i < infoResolve.size(); i++) {
            ResolveInfo resolveInfo = infoResolve.get(i);
            String intentPName = resolveInfo.activityInfo.packageName;
            //finding for facebook intent
            if (intentPName.contains("facebook")) {
                Intent fbIntent = new Intent();
                fbIntent.setComponent(new ComponentName(intentPName, resolveInfo.activityInfo.name));
                fbIntent.setAction(Intent.ACTION_SEND);
                fbIntent.setType("image/jpg");
                //adding music file in intent extras
                fbIntent.putExtra(Intent.EXTRA_STREAM, musicUri);
                listOfIntents.add(new LabeledIntent(fbIntent, intentPName, resolveInfo.loadLabel(packageManager), resolveInfo.icon));
            }
        }
        LabeledIntent[] intentsToRemove = listOfIntents.toArray(new LabeledIntent[listOfIntents.size()]);
        //adding the chosen intents to list
        chooserOptions.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentsToRemove);
        //starting chooser activity
        startActivity(chooserOptions);
    }

    //creting custom bar for application
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
