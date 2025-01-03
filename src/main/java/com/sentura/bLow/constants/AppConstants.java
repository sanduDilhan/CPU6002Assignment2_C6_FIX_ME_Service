package com.sentura.bLow.constants;

public class AppConstants {
    public static final String emailValidationRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    public static final String SERVER_ERROR = "Internal Server Error";
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    public enum UserStatus {
        PENDING,REJECT,ACCEPTED
    }
}
