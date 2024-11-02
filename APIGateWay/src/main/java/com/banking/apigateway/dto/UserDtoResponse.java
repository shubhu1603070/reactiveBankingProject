package com.banking.apigateway.dto;



import com.banking.apigateway.enums.Role;

import java.util.List;


public class UserDtoResponse {
    String userId;
    String email;
    String username;
    String password;
    String amount;
    List<Role> role;

    public UserDtoResponse(String userId, String email, String username, String password, String amount, List<Role> role) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.amount = amount;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }
}