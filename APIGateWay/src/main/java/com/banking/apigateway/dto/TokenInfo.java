package com.banking.apigateway.dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TokenInfo {
    String userId;
    String username;
    String token;
    private Date issuedAt;
    private Date expiresAt;

}
