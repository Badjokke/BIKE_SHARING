package com.example.bike_sharing.validator;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_PATTERN = "^([A-Za-z0-9-]+\\@[A-Za-z0-9-]+\\.[A-Za-z0-9-]{2,})$";

    public static boolean isValidEmailAddress(String emailAddress){
        if(emailAddress == null || emailAddress.length() == 0)
            return false;
        return patternMatches(emailAddress,EMAIL_PATTERN);
    }



    private static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
