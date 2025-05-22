package com.aba_payment.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@Entity
@Getter
@Setter
@ToString
@Table(name = "user_info", schema = "myapps")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "exp_date")
    private Timestamp expDate;

    @CreationTimestamp
    @Column(name = "cre_date", updatable = false)
    private Timestamp createDate;

    @Column(name = "active", columnDefinition = "boolean default true")
    private Boolean active = true;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 10)
    private Role role;

    public enum Role {
        ADMIN, USER
    }

    @PrePersist
    public void beforeSave() {
        long thirtyDaysInMillis = TimeUnit.DAYS.toMillis(30);

        this.createDate = new Timestamp(System.currentTimeMillis());
        this.expDate = new Timestamp(System.currentTimeMillis() + thirtyDaysInMillis); // 30 Days
    }

}
