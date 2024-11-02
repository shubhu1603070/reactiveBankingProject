package com.banking.accountmicroservice.exception;

public class ApiException extends RuntimeException{

    String errorStatus;

    ApiException(String message,String status){
        super(message);
        this.errorStatus = status;
    }

}
