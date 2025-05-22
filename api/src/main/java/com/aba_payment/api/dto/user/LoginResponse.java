package com.aba_payment.api.dto.user;

import com.aba_payment.api.dto.StatusResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Getter @Setter
public class LoginResponse extends StatusResponse {

    String username;

    Timestamp createDate;

    Timestamp expDate;

    Boolean active;

    String token;

    Timestamp tokenExp;

    public LoginResponse() {}

    public LoginResponse responseError(String message) {
        LoginResponse response = new LoginResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(message);
        return response;
    }

}
