package com.banking.apigateway.securityconfiguration.security.auth;

import com.banking.apigateway.exceptions.APIException;

public class UnauthorizedException extends APIException {
    public UnauthorizedException(String message) {
        super(message,"");
    }
}
