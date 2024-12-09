package com.springboot.obbm.respository;

import com.springboot.obbm.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer> {
    @Query("SELECT COALESCE(SUM(p.amountPaid), 0) FROM paymenthistory p WHERE p.contract.contractId = :contractId")
    Optional<Double> findTotalPaidByContract_ContractId(@Param("contractId") Integer contractId);
    boolean existsByOrderCode(Long orderCode);

    List<PaymentHistory> findAllByContract_ContractIdOrderByCreatedAtDesc(Integer contractId);
}