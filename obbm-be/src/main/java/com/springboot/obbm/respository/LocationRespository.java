package com.springboot.obbm.respository;

import com.springboot.obbm.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRespository extends JpaRepository<Location, Integer> {
    Page<Location> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Location> findByLocationIdAndDeletedAtIsNull(int id);
}
