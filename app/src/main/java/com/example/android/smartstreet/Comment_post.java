package com.example.android.smartstreet;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


public class Comment_post extends AppCompatActivity {
    String firstName = "";
    EditText editText;
    RatingBar ratingBar;
    Context context = this;
    CommentDbHelper commentDbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor commentCursor;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_post);
        firstName = getIntent().getStringExtra("firstname");
        customActionBar();
        userName = (TextView) findViewById(R.id.user_name);
        userName.setText(firstName);
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

    public void newComment(View view) {
        editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        double rating = ratingBar.getRating();
        message = "\n" + message + "\nRating: " + rating + "\n\t-" + firstName;

        commentDbHelper = new CommentDbHelper(context);
        //getting database instance to write data
        sqLiteDatabase = commentDbHelper.getWritableDatabase();
        commentDbHelper.addInformations(message, sqLiteDatabase);
        //toast will appear after successful database insertion
        Toast.makeText(getBaseContext(), "Comment Saved", Toast.LENGTH_LONG).show();
        //database closing
        commentDbHelper.close();
        editText.setText("");
    }
}
