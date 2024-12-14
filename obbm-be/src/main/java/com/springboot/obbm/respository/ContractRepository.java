package com.springboot.obbm.respository;

import com.springboot.obbm.model.Contract;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    Page<Contract> findAllByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);

    Optional<Contract> findByContractIdAndDeletedAtIsNull(int id);
    Optional<Contract> findTopByUsers_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(String users_userId);
    Page<Contract> findAllByUsers_UserIdAndDeletedAtIsNull(String userId, Pageable pageable);

    @Query("SELECT c FROM contract c WHERE c.status = :status AND c.updatedAt BETWEEN :startDate AND :endDate AND c.deletedAt IS NULL")
    List<Contract> findContractsByStatusAndDateRange(
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT c FROM contract c WHERE c.status = :status AND c.updatedAt BETWEEN :startDate AND :endDate AND c.deletedAt IS NULL")
    Page<Contract> findPageContractsByStatusAndDateRange(
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("SELECT c FROM contract c WHERE c.updatedAt BETWEEN :startDate AND :endDate AND c.deletedAt IS NULL")
    List<Contract> findContractsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}