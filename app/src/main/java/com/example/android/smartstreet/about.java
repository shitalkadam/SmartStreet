package com.example.android.smartstreet;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class about extends AppCompatActivity {

    static final String BARCODE_SCAN = "com.google.zxing.client.android.SCAN";
    TextView content_text;
    ImageButton logoButton;

    showDialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        content_text = (TextView) findViewById(R.id.content_text);
        //creating Actionbar in application
        customActionBar();
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

    public void yes(View view) {
        Uri scan_uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
        Intent scan_intent = new Intent(Intent.ACTION_VIEW, scan_uri);
        try {
            startActivity(scan_intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void no(View view) {
        dialog.cancel();

    }

    /**
     * This method is invoked when scan button is clicked.
     * It stars the new intent for scanning the barcode multiple types.
     */
    public void scan(View view) {
        Intent intent = new Intent(BARCODE_SCAN);
        try {
            intent.putExtra("SCAN_FORMATS", "QR_CODE_MODE,PRODUCT_MODE,DATA_MATRIX,PDFA17,CODE_39,AZTEC,CODE_93,CODE_128,CODABAR,ITF,");
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            //if scanner is not present dialogbox will appear to ask the permission of user to download the scanner application
            dialog = new showDialog(this);
            dialog.show();
        }
    }

    /**
     * This method will get the scan intent extras such as contents and format and toast will be displayed with the scan information
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String code_contents = intent.getStringExtra("SCAN_RESULT");
                String code_format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast info_toast = Toast.makeText(this, "Content:" + code_contents + " Format:" + code_format, Toast.LENGTH_SHORT);
                info_toast.show();
                content_text.setText("Contents:" + "\n" + code_contents + "\n" + "\n" + "Format:" + code_format);
            }
        }

        //if somehow scan contents are not found following Toast will be displayed
        else {
            Toast info_toast = Toast.makeText(this, "Contents not found", Toast.LENGTH_SHORT);
            info_toast.show();
        }
    }

    /**
     * AlertDialog will appear if users phone don't have the scanner application
     */
     class showDialog extends Dialog {

        public showDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setContentView(R.layout.alertdialog);
            Window window = getWindow();
            window.setGravity(Gravity.CENTER);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onStop() {
            super.onStop();
        }
    }
}
