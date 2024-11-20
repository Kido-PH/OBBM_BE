package com.springboot.obbm.respository;

import com.springboot.obbm.model.Permission;
import com.springboot.obbm.model.Role;
import com.springboot.obbm.model.User;
import com.springboot.obbm.model.UserRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolePermissionRepository extends JpaRepository<UserRolePermission, Integer> {
    boolean existsByUsers(User user);

    List<UserRolePermission> findByUsers(User users);

    void deleteByUsers(User user);
}
