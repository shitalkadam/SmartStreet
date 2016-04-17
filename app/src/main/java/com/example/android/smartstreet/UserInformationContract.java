package com.example.android.smartstreet;

/**
 * Created by shital on 3/14/16.
 */
public class UserInformationContract {
    //interface for user database
    public static abstract class UserInfo{
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String USER_NAME = "user_name";
        public static final String USER_PASSWORD = "user_password";
        public static final String USER_PHONE = "user_phone";
        public static final String USER_EMAIL = "user_email";
        public static final String TABLE_NAME = "user_information";
    }
}
