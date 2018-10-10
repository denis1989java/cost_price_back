package com.monich.cost_price.core.repository;

import com.monich.cost_price.core.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;


public interface UserRepository extends Repository<User, Long> {

    User findOneByPrincipalEmail(String email);

    User findOneByEmailToken(String token);

    User findOneByRequestId(UUID uuid);

    @Modifying
    @Query("delete from User u where u.id = :id")
    void delete(@Param("id") Long userId);

    <T extends User> T save(T user);

    long count();

    Collection<User> findAll();
}
