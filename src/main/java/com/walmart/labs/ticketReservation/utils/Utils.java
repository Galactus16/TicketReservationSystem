package com.walmart.labs.ticketReservation.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class for helper functions
 */
public class Utils {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Method to validate a given email
     * @param emailStr - emailId of the user
     * @return boolean : true if valid email else false
     */
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


}
