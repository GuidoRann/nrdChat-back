package com.nrdChat.app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nrdChat.app.enums.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Integer statusCode;
    private String error;
    private String message;

    private Long id;
    private String name;
    private String email;
    private UserState userState;
    private String role;

    private String token;
    private String refreshToken;
    private String expirationTime;
}
