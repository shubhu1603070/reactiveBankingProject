package com.banking.usermicroservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class FindUserByUserNameRequest {
    String username;
}
