package com.monich.cost_price.core.predicate;

import com.monich.cost_price.core.domain.QAccount;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

public class AccountPredicateBuilder {

    private BooleanExpression labelExpression;
    private BooleanExpression balanceExpression;
    private BooleanExpression currencyCodeExpression;

    public AccountPredicateBuilder withLabel(String label) {
        if (label != null) {
            labelExpression = QAccount.account.label.contains(label);
        }
        return this;
    }

    public AccountPredicateBuilder withBalance(Long from, Long to) {
        if (from != null && to != null) {
            this.balanceExpression = QAccount.account.balance.between(from, to);
        } else if (from != null) {
            this.balanceExpression = QAccount.account.balance.between(from, Long.MAX_VALUE);
        } else if (to != null) {
            this.balanceExpression = QAccount.account.balance.between(Long.MIN_VALUE, to);
        }
        return this;
    }

    public AccountPredicateBuilder withCurrencyCode(String currencyCode) {
        if (currencyCode != null) {
            this.currencyCodeExpression = QAccount.account.currencyCode.eq(currencyCode);
        }
        return this;
    }


    public Predicate build() {
        return ExpressionUtils.allOf(labelExpression,
                balanceExpression,
                currencyCodeExpression
        );
    }

}
