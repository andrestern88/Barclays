package com.example.barclays.exceptions;

public class UserHasAccountException extends Exception{
    public UserHasAccountException(String errorMessage) {
        super(errorMessage);
    }
}
