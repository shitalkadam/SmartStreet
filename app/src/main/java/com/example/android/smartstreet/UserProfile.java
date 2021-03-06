package com.example.android.smartstreet;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by sesharika on 4/25/16.
 */
public class UserProfile {

    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    public String getKey() {
        return key;
    }


    //to add to the git
    public void setKey(String key) {
        this.key = key;
    }

    @JsonIgnore
    private String key;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserProfile(String key, String email, String firstName, String lastName, String phone){

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
    public UserProfile(){
    }
}
