package com.springboot.obbm.respository;

import com.springboot.obbm.model.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    int deleteByExpiryTimeBefore(Date expiryTime);
}
