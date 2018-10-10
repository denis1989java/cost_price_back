package com.monich.cost_price.core.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Currency;

@Entity
@Table(name = "account")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_number", nullable = false, length = 20, unique = true)
    private String accountNumber;

    @Column(name = "label")
    private String label;

    @Column(name = "balance")
    private long balance;

    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;

    @Transient
    public Currency getCurrency() {
        return Currency.getInstance(currencyCode);
    }
}
