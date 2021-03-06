package com.example.android.smartstreet;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    static final String SCAN_BARCODE = "com.google.zxing.client.android.SCAN";
    EditText firstNameText,lastNameText,emailText,passwordText,phoneText;
    Context context = this;

    private static final String URL = "https://smartstreetapp.firebaseio.com";
    Firebase ref;

    String code_contents;
    String firstName;
    String lastName;
    String phone;
    String email;
    String password;
    String fname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        customActionBar();
        Firebase.setAndroidContext(this);
        ref = new Firebase(URL);
       //creating the registration form
        createViews();
    }

    //to add to the git

    private void createViews() {
        firstNameText = (EditText) findViewById(R.id.firstName);
        //adding the text chage listener to validate text entry
        firstNameText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                UserValidationHelper.validFirstName(firstNameText, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        lastNameText = (EditText) findViewById(R.id.lastName);
        //adding the text chage listener to validate text entry
        lastNameText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                UserValidationHelper.validFirstName(lastNameText, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        emailText = (EditText) findViewById(R.id.email);
        //adding the text chage listener to validate text entry
        emailText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                UserValidationHelper.validEmailAddress(emailText, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        passwordText = (EditText) findViewById(R.id.password);
        //adding the text chage listener to validate text entry
        passwordText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                UserValidationHelper.validPassword(passwordText, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        phoneText = (EditText) findViewById(R.id.phone);
        //adding the text chage listener to validate text entry
        phoneText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                UserValidationHelper.validPhoneNumber(phoneText, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

//        Button btnSubmit = (Button) findViewById(R.id.submit_bt);
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /*
//                Validation class will check the error and display the error on respective fields
//                but it won't resist the form submission, so we need to check again before submit
//                 */
//                if (validationCkeck()) {
//                    formSubmit();
//                }
//                else
//                    Toast.makeText(Registration.this, "Form contains error", Toast.LENGTH_LONG).show();
//            }
//        });
    }
    //method is invoked when adding user information into database
    private void formSubmit() {

        //getting data form views

        firstName = firstNameText.getText().toString();
        lastName = lastNameText.getText().toString();
        password = passwordText.getText().toString();
        phone = phoneText.getText().toString();
        email = emailText.getText().toString();

        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                Toast.makeText(getBaseContext(), "Registered successfully!", Toast.LENGTH_LONG).show();

                String uid = result.get("uid").toString();
                Map<String, String> map = new HashMap<String, String>();
                map.put("firstName", firstName);
                map.put("lastName", lastName);
                map.put("phone", phone);
                map.put("email", email);
                ref.child("users").child(uid).setValue(map);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                System.out.println("UN Successfully created user account with uid: ");
                Toast.makeText(getBaseContext(), firebaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }
    //validating the entered fields
    private boolean validationCkeck() {
        boolean reValue = true;

        if (!UserValidationHelper.validFirstName(firstNameText, true)) reValue = false;
        if (!UserValidationHelper.validEmailAddress(emailText, true)) reValue = false;
        if (!UserValidationHelper.validPassword(passwordText, true)) reValue = false;
        if (!UserValidationHelper.validPhoneNumber(phoneText, true)) reValue = false;

        return reValue;
    }
   //adding actionbar to application
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
    //method will invoke when user submits form
    public void submit(View view){
        if (validationCkeck()) {
            formSubmit();
            AuthData authData = ref.getAuth();
            if (authData != null) {
                Intent home_intent = new Intent(this, MainActivity.class);
                home_intent.putExtra("firstname", firstName);
                startActivity(home_intent);
                this.finish();
            } else {
                //Toast.makeText(getBaseContext(), "UnRegistered successfully!", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(Registration.this, "Form contains error", Toast.LENGTH_LONG).show();
    }
   //launches the home page
    public void home(View view){
        Intent home_intent = new Intent(this,MainActivity.class);
        startActivity(home_intent);
        this.finish();
    }
    //scanning the barcode for registration
    public void barcodeRegister(View view){
        Intent scan_intent = new Intent(SCAN_BARCODE);
        try {
            scan_intent.putExtra("SCAN_FORMATS", "QR_CODE_MODE,PRODUCT_MODE,DATA_MATRIX,AZTEC,CODE_93,CODE_128,CODABAR,ITF,");
            startActivityForResult(scan_intent, 1);
        } catch (ActivityNotFoundException e) {
            //if scanner is not present dialogbox will appear to ask the permission of user to download the scanner application
           e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
               //getting the barcode contents
                code_contents = intent.getStringExtra("SCAN_RESULT");
                String code_format = intent.getStringExtra("SCAN_RESULT_FORMAT");
            //    Toast info_toast = Toast.makeText(this, "Content:" + contents + " Format:" + code_format, Toast.LENGTH_SHORT);
             //   info_toast.show();
                acquiredata();
                Intent home_intent = new Intent(this, MainActivity.class);
                home_intent.putExtra("firstname", fname);
                startActivity(home_intent);
                this.finish();
            }
        }
        //if somehow scan contents are not found following Toast will be displayed
        else {
            Toast info_toast = Toast.makeText(this, "Contents not found", Toast.LENGTH_SHORT);
            info_toast.show();
        }
    }
    //acquiring user's data from barcode contents
    public void acquiredata() {
        String textStr[] = code_contents.split("\\r?\\n");
        String lineFName = textStr[0];
        String lineLName = textStr[1];
        String lineEamil = textStr[2];
        String linePassword = textStr[3];
        String linePhone = textStr[4];


        String textStr1[] = lineFName.split(":");
        fname = textStr1[1];

        String textStr2[] = lineLName.split(":");
        final String lname = textStr2[1];

        String textStr3[] = lineEamil.split(":");
        final String email = textStr3[1];

        String textStr4[] = linePassword.split(":");
        final String password = textStr4[1];

        String textStr5[] = linePhone.split(":");
        final String phone = textStr5[1];

        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                Toast.makeText(getBaseContext(), "Registered successfully!", Toast.LENGTH_LONG).show();

                String uid = result.get("uid").toString();
                Map<String, String> map = new HashMap<String, String>();
                map.put("firstName", fname);
                map.put("lastName", lname);
                map.put("phone", phone);
                map.put("email", email);
                ref.child("users").child(uid).setValue(map);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                System.out.println("UN Successfully created user account with uid: ");
                Toast.makeText(getBaseContext(), firebaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
