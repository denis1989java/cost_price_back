package com.monich.cost_price.core.repository;

import org.springframework.data.repository.Repository;

public interface PrincipalRepository extends Repository<Principal, Long> {
    Principal save(Principal principal);

    void delete(Principal principal);

    long count();
}
