package com.banking.accountmicroservice.exception;

public class FieldMissingException extends ApiException{
    public FieldMissingException(String pleaseGiveProperDetails, String s) {
        super(pleaseGiveProperDetails,s);

    }
}
