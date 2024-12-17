package com.springboot.obbm.respository;

import com.springboot.obbm.model.IssuedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssuedTokenRepository extends JpaRepository<IssuedToken, String> {
    List<IssuedToken> findByUsername(String username);
}
