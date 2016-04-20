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

public class UserLogin extends AppCompatActivity {
    ImageButton logoButton;

    UserRegistrationHelper userRegistrationHelper;
    SQLiteDatabase sqLiteDatabase;

    String passDb;
    String firstNameDb="";
    String userNameDb;

    EditText userNameText;
    EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        customActionBar();
        userNameText = (EditText) findViewById(R.id.user_name_text);
        passwordText = (EditText) findViewById(R.id.password_text);

    }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about, menu);
        return true;
    }
    //method will invoke when user clicks on login button
    public void submit(View view){
       //getting user name from view
        String userName = userNameText.getText().toString();
        //getting password from view
        String password = passwordText.getText().toString();
       //opening the database and getting contents
        userRegistrationHelper = new UserRegistrationHelper(getApplicationContext());
        sqLiteDatabase = userRegistrationHelper.getReadableDatabase();
        Cursor cursor = userRegistrationHelper.getUser(userName,sqLiteDatabase);
        //matching the username and password
        if(cursor.moveToFirst()){
            userNameDb = cursor.getString(0);
            passDb = cursor.getString(1);
            firstNameDb = cursor.getString(2);

            if(password.equals(passDb)) {
               //if username and password matches launches the home page
                Intent home_intent = new Intent(this,MainActivity.class);
                //putting user's name with hame page intent
                home_intent.putExtra("firstname", firstNameDb);
                startActivity(home_intent);
                this.finish();
            }
            else{

                passwordText.setError("wrong password");
            }
        }
        else {
            userNameText.setError("wrong username");
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
