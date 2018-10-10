package com.monich.cost_price.api.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDto {
    @JsonProperty("account_number")
    private String accountNumber;
    private String label;
    private long balance;
    @JsonProperty("currency")
    private String currencyCode;
    private boolean main;
    private boolean linked;
}
