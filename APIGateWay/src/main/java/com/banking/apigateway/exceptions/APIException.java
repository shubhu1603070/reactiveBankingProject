package com.banking.apigateway.exceptions;

public class APIException extends RuntimeException{

    private final String errorCode;
    public APIException(String message,String statusCode){
        super(message);
        this.errorCode = statusCode;
    }

    public String getErrorCode(){
        return this.errorCode;
    }

}
