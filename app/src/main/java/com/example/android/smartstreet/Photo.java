package com.example.android.smartstreet;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Photo extends AppCompatActivity {
    static final int pictureCode = 0;
    ImageButton logoButton;
    ImageView displayImage;
    ImageButton share;
    private File photoFile;
    private Uri pictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        customActionBar();
        displayImage = (ImageView) findViewById(R.id.picture);
        share = (ImageButton) findViewById(R.id.share_bt);
        share.setEnabled(false);
    }
    // starting the camera activity for taking picture
    public void reTakePicture(View view) {
        String picturePath = null;
        String typeOfFile = null;
        Intent pictureIntent = null;
        String fileName = "smartStreet";
        //setting picture path
        picturePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Smart_Street_Media/picture";
        typeOfFile = ".jpg";
        pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File pictureDir = null;
        if (picturePath != null) {
            pictureDir = new File(picturePath);
        }
        assert pictureDir != null;
        if (!pictureDir.exists()) {
            pictureDir.mkdirs();
        }
        //getting current date and time
        String photoDateAndTime = getDateAndTimeForFile();

        photoFile = new File(pictureDir, fileName + photoDateAndTime + typeOfFile);
        //setting picture Uri
        pictureUri = Uri.fromFile(photoFile);


        if (pictureIntent != null) {
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        }
        pictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(pictureIntent, pictureCode);
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent intentData) {

        if (reqCode == pictureCode) {
            switch (resCode) {
                case Activity.RESULT_OK:
                    if (photoFile.exists()) {
                        //saving file in folder
                        saveFileInStorage(photoFile);
                        Toast.makeText(this, "The photoFile was saved at " + photoFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        //displaying picture in imageview
                        displayPicture();
                        share.setEnabled(true);
                    } else {
                        Toast.makeText(this, "There was some error", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }
    //sharing picture file with Gmail and facebook using Chooser
    public void sharePicture(View view) {

        Resources resources = getResources();

        Intent gMailIntent = new Intent();
        //setting the email intent extras
        gMailIntent.setAction(Intent.ACTION_SEND);
        gMailIntent.putExtra(Intent.EXTRA_TEXT, "Sharing Photo via Smart Street!");
        gMailIntent.putExtra(Intent.EXTRA_SUBJECT, "Photo of Smart Street");
        gMailIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
        gMailIntent.setType("message/rfc822");

        PackageManager packageManager = getPackageManager();
        //creating content of chooser to share picture
        Intent pictureIntentToAdd = new Intent(Intent.ACTION_SEND);
        pictureIntentToAdd.setType("image/jpg");
        Intent chooserForPackage = Intent.createChooser(gMailIntent, "Share picture with....");
        //getting the list of all available intents
        List<ResolveInfo> resInformation = packageManager.queryIntentActivities(pictureIntentToAdd, 0);
        List<LabeledIntent> listOfIntents = new ArrayList<LabeledIntent>();
        //loop through the list of all gathered intents
        for (int i = 0; i < resInformation.size(); i++) {

            ResolveInfo resolveInfo = resInformation.get(i);
            String packageName = resolveInfo.activityInfo.packageName;
            //finding for facebook intent
            if (packageName.contains("facebook")) {
                Intent fbIntent = new Intent();
                fbIntent.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
                fbIntent.setAction(Intent.ACTION_SEND);
                fbIntent.setType("image/jpg");
                //adding picture file in intent extras
                if (packageName.contains("facebook")) {
                    fbIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
                }
                listOfIntents.add(new LabeledIntent(fbIntent, packageName, resolveInfo.loadLabel(packageManager), resolveInfo.icon));
            }
        }
        LabeledIntent[] intentsToAvoid = listOfIntents.toArray(new LabeledIntent[listOfIntents.size()]);
        //adding the chosen intents to list
        chooserForPackage.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentsToAvoid);
        //starting chooser activity
        startActivity(chooserForPackage);
    }
    //getting date and time
    private String getDateAndTimeForFile() {
        Calendar calenderForDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return dateFormat.format(calenderForDate.getTime());
    }
    //saving file on phone storage
    private void saveFileInStorage(File file) {
        MediaScannerConnection.scanFile(this,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.e("Storage", "Scanned " + path + ":");
                        Log.e("storage", "-> pictureUri=" + uri);
                    }
                });
    }

    public void displayPicture() {
        displayImage.setImageURI(pictureUri);
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
