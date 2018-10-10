package com.monich.cost_price.core.repository;

import org.springframework.data.repository.Repository;

public interface RegisteredUserRepository extends Repository<RegisteredUser, Long> {
    RegisteredUser findByPrincipalEmail(String email);

    RegisteredUser save(RegisteredUser user);
}
