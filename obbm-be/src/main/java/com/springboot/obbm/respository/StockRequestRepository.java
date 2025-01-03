package com.springboot.obbm.respository;

import com.springboot.obbm.model.Dish;
import com.springboot.obbm.model.StockRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRequestRepository extends JpaRepository<StockRequest, Integer> {
    Page<StockRequest> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<StockRequest> findByStockrequestIdAndDeletedAtIsNull(int id);
}
