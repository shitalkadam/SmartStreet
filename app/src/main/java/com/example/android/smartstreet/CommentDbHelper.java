package com.example.android.smartstreet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shital on 3/8/16.
 */
public class CommentDbHelper extends SQLiteOpenHelper {

    //

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "COMMENTS.DB";

    private static final String CREATE_QUERY =
            "CREATE TABLE " + UserComments.Comments.TABLE_NAME + "(" + UserComments.Comments.COMMENT + " TEXT);";

    public CommentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("COMMENT DATABASE", "Database created / opened....");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating/opening database
        db.execSQL(CREATE_QUERY);
        Log.e("COMMENT DATABASE", "Table created....");
    }

    //this method will add new comment into database
    public void addInformations(String comment, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserComments.Comments.COMMENT, comment);
        db.insert(UserComments.Comments.TABLE_NAME, null, contentValues);
        Log.e("COMMENT DATABASE", "New row inserted...");
    }

    //fetching all the comments from database
    public Cursor getComments(SQLiteDatabase db) {
        Cursor commentCursor;
        String[] commentProjection = {UserComments.Comments.COMMENT};
        commentCursor = db.query(UserComments.Comments.TABLE_NAME, commentProjection, null, null, null, null, null);
        return commentCursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
