package com.springboot.obbm.respository;

import com.springboot.obbm.model.UserGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UGPRespository extends JpaRepository<UserGroupPermission, Integer> {
    @Query("SELECT ugp FROM UserGroupPermission ugp " +
            "JOIN ugp.users u " +
            "JOIN ugp.pergroups g " +
            "JOIN ugp.permissions p " +
            "WHERE u.userId = :userId")
    List<UserGroupPermission> findByUserId(@Param("userId") String userId);
}
