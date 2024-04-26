package com.auth1.auth.learning.repository;

import com.auth1.auth.learning.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    //t_id  token_expiry_date           token_Value                             user_id     deleted
    //5	   2024-05-26 22:57:40.248000	67a10907-e62f-41a4-81ef-b285c68d8d74    1             0
    Optional<Token> findByValueAndDeletedEquals(String token, Boolean isDeleted);

      /*
            To validate token
            1. check if token value is present
            2. check if token is not deleted
            3. check if token is not expired
        */

    Optional<Token> findByValueAndDeletedEqualsAndExpireAtGreaterThan(
            String token, Boolean isDeleted, Date date);
}
