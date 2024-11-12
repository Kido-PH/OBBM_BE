package com.springboot.obbm.respository;

import com.springboot.obbm.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    Page<Contract> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Contract> findByContractIdAndDeletedAtIsNull(int id);
    Optional<Contract> findTopByUsers_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(String users_userId);
    Page<Contract> findAllByUsers_UserIdAndDeletedAtIsNull(String userId, Pageable pageable);
}