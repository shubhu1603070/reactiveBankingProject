package com.banking.apigateway.securityconfiguration.security.auth;

import javax.security.auth.Subject;
import java.security.Principal;

public class MyUserPrinciple implements Principal {

    private final String id;
    private final String name;

    MyUserPrinciple(String id,String name){
        this.id = id;
        this.name = name;
    }

    public String getId(){
        return this.id;
    }

    @Override
    public String getName(){
        return this.name;
    }
}
