package com.springboot.obbm.respository;

import com.springboot.obbm.model.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Integer> {
    Page<Services> findAllByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);

    Optional<Services> findByServiceIdAndDeletedAtIsNull(int id);
}
