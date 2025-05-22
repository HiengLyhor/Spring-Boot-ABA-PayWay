package com.aba_payment.api.controller;

import com.aba_payment.api.dto.user.LoginRequest;
import com.aba_payment.api.dto.user.LoginResponse;
import com.aba_payment.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("login")
    LoginResponse login(@Valid @RequestBody LoginRequest request){
        return userService.login(request);
    }

}
