package com.aba_payment.api.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class LoginRequest {

    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be blank")
    String username;

    String password;

}
