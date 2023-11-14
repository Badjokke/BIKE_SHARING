package com.example.bike_sharing.enums;

/**
 * status code returned by user service indicating result of operation
 * @author jiri trefil
 */
public enum UserServiceStatus {

    INVALID_USER_ARGUMENTS("Invalid user arguments for registration",400),
    USER_EXISTS("User exists",400),
    USER_CREATED("User successfully created",201),
    USER_LOGGED_IN("User logged in successfully",200),
    USER_LOGIN_FAILED("User login failed",401),
    USER_LOGGED_OUT("User logged out.",200),
    INVALID_CHANGE_ROLE_ARGUMENTS("Arguments for role change are invalid!",400),
    USER_ROLE_CHANGED("Role of user has been changed",200),
    ROLE_CHANGE_FAILED("Role of a user failed to chagne",500);

    private final String label;
    private final int statusCode;

    UserServiceStatus(String s, int i) {
        this.label = s;
        this.statusCode = i;
    }

    public int getStatusCode(){
        return this.statusCode;
    }
    public String getLabel(){
        return this.label;
    }


}
