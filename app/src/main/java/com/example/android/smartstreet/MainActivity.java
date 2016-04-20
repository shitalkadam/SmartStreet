package com.example.android.smartstreet;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_MEDIA = 1;
    ImageButton logoButton;
    showDialog dialog;
    registerDialog registerDialog;
    String mediaPath;
    Uri mediaUri;

    String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = getIntent().getStringExtra("firstname");
        customActionBar();


        TextView firstNameText = (TextView) findViewById(R.id.user_name);
        firstNameText.setText(firstName);
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

    /**
     * When user clicks on "About" ImageButton about activity is called using new Intent
     * And new page will be opened.
     */

    public void about(View view) {
        Intent about_intent = new Intent(this, about.class);
        startActivity(about_intent);
    }

    /**
     * When user clicks on Places Around ImageButton Google Map activity is called using new Intent
     * And new page will be opened.
     */

    public void places(View view) {
        Intent places_intent = new Intent(this, MapsActivity.class);
        startActivity(places_intent);
    }
    //starts the comment activity
    public void comment(View view) {
        //checking if user is logged in or not
        // if not registered asks to register
        if (firstName == null) {
            registerDialog = new registerDialog(this);
            registerDialog.show();
        } else {
            //else open the comment activity
            Intent comment_intent = new Intent(this, Comment.class);
            comment_intent.putExtra("firstname", firstName);
            startActivity(comment_intent);
        }
    }
    //launches the register activity
    public void register(View view) {
        registerDialog.cancel();
        Intent register_intent = new Intent(this, Registration.class);
        startActivity(register_intent);
    }

    //if user clicks on "No, thanks" closes the alertDialog
    public void noRegister(View view) {
        registerDialog.cancel();
    }

    //shows the capture dialog
    public void capture(View view) {
        dialog = new showDialog(this);
        dialog.show();
    }
    //launches the video activity
    public void captureVideo(View view) {
        Intent video_intent = new Intent(this, Video.class);
        startActivity(video_intent);
    }
    //launches the photo activity
    public void capturePhoto(View view) {
        Intent photo_intent = new Intent(this, Photo.class);
        startActivity(photo_intent);
    }
    //launches the music activity
    public void captureMusic(View view) {
        Intent music_intent = new Intent(this, Music.class);
        startActivity(music_intent);
    }
    // opens the contents stored on user's phone to share
    public void share(View view) {
        Intent media_intent = new Intent();
        media_intent.setType("*/*");
        media_intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(media_intent,
                "Select Media..."), SELECT_MEDIA);
    }
    // on selecting the media file gets the path and converts it into Uri
    @Override
    public void onActivityResult(int reqCode, int resCode, Intent intent) {
        if (resCode == RESULT_OK) {
            if (reqCode == SELECT_MEDIA) {
                mediaUri = intent.getData();
                mediaPath = getMediaPath(mediaUri);
            }
        }

        sharePicture(mediaUri);
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_STREAM, mediaUri);
//        startActivity(Intent.createChooser(intent, "Share picture with..."));
    }

    //
    public void sharePicture(Uri mediaUri) {

        Resources resources = getResources();

        Intent gMailIntent = new Intent();
        gMailIntent.setAction(Intent.ACTION_SEND);

        gMailIntent.putExtra(Intent.EXTRA_SUBJECT, "Media of Smart Street");
        gMailIntent.putExtra(Intent.EXTRA_TEXT, "Sharing media via Smart Street!");
        gMailIntent.putExtra(Intent.EXTRA_STREAM, mediaUri);
        gMailIntent.setType("message/rfc822");

        PackageManager packageManager = getPackageManager();
        Intent sendMediaIntent = new Intent(Intent.ACTION_SEND);
        sendMediaIntent.setType("*/*");
        Intent chooserList = Intent.createChooser(gMailIntent, "Share media with....");

        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(sendMediaIntent, 0);
        List<LabeledIntent> listOfIntents = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resolveInfos.size(); i++) {
            ResolveInfo resolveInfo = resolveInfos.get(i);
            String nameOfPackageToAdd = resolveInfo.activityInfo.packageName;
            if (nameOfPackageToAdd.contains("facebook")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(nameOfPackageToAdd, resolveInfo.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/jpg");

                if (nameOfPackageToAdd.contains("facebook")) {
                    intent.putExtra(Intent.EXTRA_STREAM, mediaUri);
                }
                listOfIntents.add(new LabeledIntent(intent, nameOfPackageToAdd, resolveInfo.loadLabel(packageManager), resolveInfo.icon));
            }
        }
        LabeledIntent[] intentsToAvoid = listOfIntents.toArray(new LabeledIntent[listOfIntents.size()]);

        chooserList.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentsToAvoid);
        startActivity(chooserList);
    }

    //getting the path of selected media file
    public String getMediaPath(Uri mediaUri) {
        if (mediaUri == null) {
            return null;
        }
        String[] mediaProjection = {MediaStore.Images.Media.DATA};
        Cursor mediaCursor = managedQuery(mediaUri, mediaProjection, null, null, null);
        if (mediaCursor != null) {
            int index = mediaCursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            mediaCursor.moveToFirst();
            return mediaCursor.getString(index);
        }
        return mediaUri.getPath();
    }

    //displaying the dialog for capture options
    class showDialog extends Dialog {

        public showDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.cature_options);
            super.onCreate(savedInstanceState);
            Window captureWindow = getWindow();
           //setting the window position
            captureWindow.setGravity(Gravity.CENTER);
        }

        @Override
        protected void onStop() {
            super.onStop();
        }

    }
    //displaying register dialog
    class registerDialog extends Dialog {

        public registerDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //setting the layout
            setContentView(R.layout.register_option);
            super.onCreate(savedInstanceState);
            Window registerWindow = getWindow();
            registerWindow.setGravity(Gravity.CENTER);
        }

        @Override
        protected void onStop() {
            super.onStop();
        }

    }
}
