package com.example.android.smartstreet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sesharika on 4/24/16.
 */
public class ReviewArrayAdapter extends BaseAdapter {

    private static final String QUOTES_PATH = "https://smartstreetapp.firebaseio.com/reviews";
    private final LayoutInflater mInflater;
    private List<UserReview> mUserReviews;
    private Firebase mUserReviewref;

    public ReviewArrayAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mUserReviews = new ArrayList<UserReview>();
        Firebase.setAndroidContext(context);
        mUserReviewref = new Firebase(QUOTES_PATH);
        mUserReviewref.addChildEventListener(new QuotesChildEventListener());

    }
    public class QuotesChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            UserReview quote = dataSnapshot.getValue(UserReview.class);
            quote.setKey(dataSnapshot.getKey());
            mUserReviews.add(0, quote);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }

    @Override
    public int getCount() {
        return mUserReviews.size();
    }

    public void removeItem(UserReview userReview) {
        //TODO: Remove data from Firebase
    }

    public void addItem(UserReview userReview) {
        //Done: Push new data to Firebase
        mUserReviewref.push().setValue(userReview);
    }

    public void updateItem(UserReview userReview, String newUsername, String newReview, float rating) {
        //TODO: Push changes to Firebase
    }

    @Override
    public UserReview getItem(int position) {
        return mUserReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        RatingBar rate;
        ReviewViewWrapper wrapper;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.row, parent, false);
        } else {
            view = convertView;
        }

        wrapper=new ReviewViewWrapper(view);
        view.setTag(wrapper);
        rate = wrapper.getRatingBar();

        UserReview userReview = getItem(position);
        wrapper.getLabel().setText(userReview.getUsername().toString());
        rate.setTag(new Integer(position));
        rate.setRating(userReview.getRating().floatValue());
        wrapper.getCommnet().setText(userReview.getReview().toString());
        return view;
    }
}

