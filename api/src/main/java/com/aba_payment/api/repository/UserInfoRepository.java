package com.aba_payment.api.repository;

import com.aba_payment.api.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByUsername(String username);

}
