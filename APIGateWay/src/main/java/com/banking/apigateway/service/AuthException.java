package com.banking.apigateway.service;


import com.banking.apigateway.exceptions.APIException;

/**
 * AuthException class
 *
 * @author Erik Amaru Ortiz
 */
public class AuthException extends APIException {
    public AuthException(String message, String errorCode) {
        super(message, errorCode);
    }
}
