package com.example.android.smartstreet;

import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by shital on 3/14/16.
 */
public class UserValidationHelper {

    //field validation regular expression
    private static final String FIRSTNAME_PATTERN = "^[a-zA-Z\\s]*$";
    private static final String PASSWORD_PATTERN = "^[a-z0-9._-]{2,25}$";
    private static final String PHONE_PATTEREN = "\\d{3}-\\d{7}";
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    //adding error messages for each field
    private static final String MSG_REQUIRED = "Required";
    private static final String MSG_FIRSTNAME = "Invalid name";
    private static final String MSG_USERNAME = "Invalid user anme";
    private static final String MSG_PASSWORD = "Invalid password";
    private static final String MSG_PHONE = "###-#######";
    private static final String MSG_EMAIL = "Invalid email";

    //validating first name
    public static boolean validFirstName(EditText editText, boolean required) {
        return fieldValidation(editText, FIRSTNAME_PATTERN, MSG_FIRSTNAME, required);
    }
    //validating password
    public static boolean validPassword(EditText editText, boolean required) {
        return fieldValidation(editText, PASSWORD_PATTERN, MSG_PASSWORD, required);
    }
    //validating phone number
    public static boolean validPhoneNumber(EditText editText, boolean required) {
        return fieldValidation(editText, PHONE_PATTEREN, MSG_PHONE, required);
    }
    //validating email address
    public static boolean validEmailAddress(EditText editText, boolean required) {
        return fieldValidation(editText, EMAIL_PATTERN, MSG_EMAIL, required);
    }
    //validation for each field
    public static boolean fieldValidation(EditText text, String pattern, String msg, boolean requiredValue) {

        String textValue = text.getText().toString().trim();

        text.setError(null);

        if ( requiredValue && !textAvailable(text) ) return false;


        if (requiredValue && !Pattern.matches(pattern, textValue)) {
            text.setError(msg);
            return false;
        };

        return true;
    }
    //checking if field is null
    public static boolean textAvailable(EditText editText) {

        String enteredTextValue = editText.getText().toString().trim();
        editText.setError(null);

        if (enteredTextValue.length() == 0) {
            editText.setError(MSG_REQUIRED);
            return false;
        }
        return true;
    }

}
