package com.banking.accountmicroservice.exception;

public class RequestInvalidException extends ApiException{
    public RequestInvalidException(String pleaseGiveProperDetails, String s) {
        super(pleaseGiveProperDetails,s);

    }
}
