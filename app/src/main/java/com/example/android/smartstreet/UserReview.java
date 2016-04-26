package com.example.android.smartstreet;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by sesharika on 4/24/16.
 */
public class UserReview {

    private String username;
    private String review;
    private Float rating;

    @JsonIgnore
    private String key;

    public UserReview(String key, String username, String review, Float rating){

        this.username = username;
        this.review = review;
        this.rating = rating;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public UserReview(){
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getKey(){
        return key;
    }

    public void setReview(String review){
        this.review = review;
    }

    public String getReview(){
        return review;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}
