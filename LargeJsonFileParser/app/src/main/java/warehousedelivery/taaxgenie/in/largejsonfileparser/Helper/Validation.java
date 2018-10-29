package warehousedelivery.taaxgenie.in.largejsonfileparser.Helper;

import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
  Created by dhwanik on 21/04/17.
 */

class Validation {

    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "^[+]?[0-12]{10,13}$";
    private static final String MOBILENO_REGEX = "^[+]?[0-9]{10,13}$";
    private static final String PANCARD_REGEX = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

    // Error Messages
    private static final String REQUIRED_MSG = "required";
    private static final String USERNAME_MSG = "User name should be 6 letters";
    private static final String EMAIL_MSG = "Please Enter Valid Email";
    private static final String PHONE_MSG = "Please Enter Currect No";
    private static final String PAN_MSG = "Please Enter valid PAN No.";

    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    // call this method when you need to check phone number validation
    public static boolean isMobileNumber(EditText editText, boolean required) {

        //System.out.println("XXXXX"+required);
        return isValid(editText, MOBILENO_REGEX, PHONE_MSG, required);
    }


    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {

        //System.out.println("XXXXX"+required);
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }
    public static boolean isPancard(EditText editText, boolean required){

        return isValid(editText,PANCARD_REGEX,PAN_MSG,required);
    }
    // return true if the input field is valid, based on the parameter passed
    private static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        }

        return true;
    }


    public static boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }
    // check the input field has any text or not
    // return true if it contains text otherwise false
    private static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }


    public static boolean validuserName(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length()<6) {
            editText.setError(USERNAME_MSG);
            return false;
        }

        return true;
    }

    public static boolean hasTextview(TextView textView) {

        String text = textView.getText().toString().trim();
        textView.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            textView.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }
}
