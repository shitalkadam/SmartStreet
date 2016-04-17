package com.example.android.smartstreet;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Comment extends AppCompatActivity {
    Context context = this;
    CommentDbHelper commentDbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor commentCursor;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapterComment;
    ListView list;
    EditText editText;
    RatingBar ratingBar;
    ImageButton logoButton;
    TextView userName;
    String firstName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        customActionBar();
        firstName = getIntent().getStringExtra("firstname");
        customActionBar();

        userName = (TextView) findViewById(R.id.user_name);
        userName.setText(firstName);
        list = (ListView) findViewById(R.id.list_view);
        customActionBar();
        addComments();
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
        logoButton = (ImageButton) View.findViewById(R.id.image_bt);
        logoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //        Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_LONG).show();
            }
        });
        if (myActionBar != null) {
            myActionBar.setCustomView(View);
        }
        if (myActionBar != null) {
            myActionBar.setDisplayShowCustomEnabled(true);
        }
    }
    // this function takes new comment information from editText, rating and adds to the Comment database.
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
        adapterComment.clear();
        addComments();
    }
    //this method will add the all the commemts from database in a listview
    public void addComments() {
        commentDbHelper = new CommentDbHelper(getApplicationContext());
        sqLiteDatabase = commentDbHelper.getReadableDatabase();
        commentCursor = commentDbHelper.getComments(sqLiteDatabase);
        //getting comment from database
        if (commentCursor.moveToFirst()) {
            do {
                String comment;
                comment = commentCursor.getString(0);
                listItems.add(comment);

            } while (commentCursor.moveToNext());
        }

        adapterComment = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapterComment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
