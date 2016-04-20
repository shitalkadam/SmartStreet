package com.example.android.smartstreet;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.MediaController;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Video extends AppCompatActivity {
    ImageButton logoButton;
    static final int videoCode = 0;
    private File videofile;
    private Uri videoUri;
    ImageButton share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customActionBar();
        setContentView(R.layout.activity_video);

        share = (ImageButton) findViewById(R.id.share_bt);
 //       share.setEnabled(false);
    }
    // starting the camera activity for recording video
    public void takeVideo(View view) {
        String video_path = null;
        String videoType = null;
        Intent videoIntent = null;
        String fileName = "smartStreet";
        //setting video path
        video_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Smart_Street_Media/video";
       //setting video type
        videoType = ".mp4";
        videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        File videoDir = null;
        if (video_path != null) {
            videoDir = new File(video_path);
        }
        assert videoDir != null;
        if (!videoDir.exists()) {
            videoDir.mkdirs();
        }
        //getting current date and time
        String videoDateAndTime = getVideoDateAndTime();

        videofile = new File(videoDir, fileName + videoDateAndTime + videoType);
        //setting video Uri
        videoUri = Uri.fromFile(videofile);


        if (videoIntent != null) {
            videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        }
        videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(videoIntent, videoCode);

    }
    //playing recorded video
    public void playVideo() {
        VideoView myVideoView = (VideoView) findViewById(R.id.myVideo);
        myVideoView.setVideoURI(videoUri);
        //starting video view
        myVideoView.start();
        //getting video controller
        MediaController myControl = new MediaController(this);
        myControl.setAnchorView(myVideoView);
        myVideoView.setMediaController(myControl);
    }
    //getting date and time
    private String getVideoDateAndTime() {
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return dateFormat.format(calender.getTime());
    }
    //saving video in phone's memory
    private void fileWasSavedInFolder(File file) {
        MediaScannerConnection.scanFile(this,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.e("Storage", "Scanned " + path + ":");
                        Log.e("Storage", "-> videoUri=" + uri);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == videoCode) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (videofile.exists()) {
                        //saving file in folder
                        fileWasSavedInFolder(videofile);
                        Toast.makeText(this, "The videofile was saved at " + videofile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        playVideo();
                    } else {
                        Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }
    //sharing video file with Gmail and facebook using Chooser
    public void shareVideo(View view) {

        Resources resources = getResources();

        Intent gMailIntent = new Intent();
        gMailIntent.setAction(Intent.ACTION_SEND);
        gMailIntent.putExtra(Intent.EXTRA_SUBJECT, "Video of Smart Street");
        gMailIntent.putExtra(Intent.EXTRA_TEXT, "Sharing Video via Smart Street!");
        gMailIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
        gMailIntent.setType("message/rfc822");

        PackageManager packageManager = getPackageManager();
        //creating content of chooser to share picture
        Intent videoIntent = new Intent(Intent.ACTION_SEND);
        videoIntent.setType("video/mp4");
        Intent chooserForPackage = Intent.createChooser(gMailIntent, "Share video with....");
        //getting the list of all available intents
        List<ResolveInfo> resInformation = packageManager.queryIntentActivities(videoIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        //loop through the list of all gathered intents
        for (int i = 0; i < resInformation.size(); i++) {

            ResolveInfo resolveInfo = resInformation.get(i);
            String packageName = resolveInfo.activityInfo.packageName;
            //finding for facebook intent
            if (packageName.contains("facebook")) {
                Intent fbIntent = new Intent();
                fbIntent.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
                fbIntent.setAction(Intent.ACTION_SEND);
                fbIntent.setType("video/mp4");
                //adding video file in intent extras
                if (packageName.contains("facebook")) {
                    fbIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
                }
                intentList.add(new LabeledIntent(fbIntent, packageName, resolveInfo.loadLabel(packageManager), resolveInfo.icon));
            }
        }

        LabeledIntent[] intentsToAvoid = intentList.toArray(new LabeledIntent[intentList.size()]);

        chooserForPackage.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentsToAvoid);
        startActivity(chooserForPackage);
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
