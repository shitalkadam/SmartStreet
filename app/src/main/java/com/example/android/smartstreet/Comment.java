package com.example.android.smartstreet;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

        list = (ListView) findViewById(R.id.list_view);
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
    // this function takes new comment information from editText, rating and adds to the Comment database.

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

    public void postcomment(View view){
        Intent comment_intent = new Intent(this, Comment_post.class);
        comment_intent.putExtra("firstname", firstName);
        startActivity(comment_intent);
    }
}
