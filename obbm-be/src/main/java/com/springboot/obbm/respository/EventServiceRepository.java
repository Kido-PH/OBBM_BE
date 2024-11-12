package com.springboot.obbm.respository;

import com.springboot.obbm.model.EventServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventServiceRepository extends JpaRepository<EventServices, Integer> {
    Page<EventServices> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<EventServices> findByEventserviceIdAndDeletedAtIsNull(int id);

    Page<EventServices> findAllByEvents_EventIdAndDeletedAtIsNull(int menuId, Pageable pageable);

    Page<EventServices> findAllByServices_ServiceIdAndDeletedAtIsNull(int dishId, Pageable pageable);
}
