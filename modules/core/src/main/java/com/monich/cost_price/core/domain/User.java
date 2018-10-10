package com.monich.cost_price.core.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Currency;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true, updatable = false, length = 50)
    private String email;

    @Column(name = "phone", unique = true, length = 50)
    private String phone;

    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;

    @Transient
    public Currency getCurrency() {
        return Currency.getInstance(currencyCode);
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;
}
