package com.aba_payment.api.service;

import com.aba_payment.api.config.JwtService;
import com.aba_payment.api.dto.user.LoginRequest;
import com.aba_payment.api.dto.user.LoginResponse;
import com.aba_payment.api.model.UserInfo;
import com.aba_payment.api.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInfoRepository userInfoRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        try{

            LoginResponse response = new LoginResponse();

            Authentication authentication = authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            BeanUtils.copyProperties(request, response);

            if (authentication.isAuthenticated()) {

                String token = jwtService.generateToken(request.getUsername());

                UserInfo userInfo = userInfoRepository.findByUsername(request.getUsername());

                BeanUtils.copyProperties(userInfo, response);
                response.setCode(HttpStatus.OK.value());
                response.setMessage("Login successfully.");
                response.setToken(token);
                response.setTokenExp(new Timestamp(System.currentTimeMillis() + 600000));

                return response;
            }

            response.setCode(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("Login failed.");

            return response;

        }
        catch (Exception ex) {
            return new LoginResponse().responseError(ex.getMessage());
        }

    }

}
