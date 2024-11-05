package com.springboot.obbm.respository;

import com.springboot.obbm.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRespository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Event> findByEventIdAndDeletedAtIsNull(int id);
}
