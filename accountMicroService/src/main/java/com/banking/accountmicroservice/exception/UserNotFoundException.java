package com.banking.accountmicroservice.exception;

public class UserNotFoundException extends ApiException{
    public UserNotFoundException(String s,String errorReason) {
        super(s,errorReason);
    }
}
