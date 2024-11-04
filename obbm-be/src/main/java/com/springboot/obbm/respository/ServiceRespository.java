package com.springboot.obbm.respository;

import com.springboot.obbm.models.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRespository extends JpaRepository<Service, Integer> {
    Page<Service> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Service> findByServiceIdAndDeletedAtIsNull(int id);
}
