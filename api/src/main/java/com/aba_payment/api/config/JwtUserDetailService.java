package com.aba_payment.api.config;

import com.aba_payment.api.model.UserInfo;
import com.aba_payment.api.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class JwtUserDetailService implements UserDetailsService {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserInfo userInfo = userInfoRepository.findByUsername(username);

        if (Objects.isNull(userInfo)) { throw new UsernameNotFoundException("No user found."); }

        return new UserPrincipal(userInfo);
    }
}
