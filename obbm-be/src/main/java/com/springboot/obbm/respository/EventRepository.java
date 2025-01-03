package com.springboot.obbm.respository;

import com.springboot.obbm.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);

    Optional<Event> findByEventIdAndDeletedAtIsNull(int id);

    Optional<Event> findTopByUsers_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(String users_userId);

    Page<Event> findAllByUsers_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(String users_userId, Pageable pageable);

    Page<Event> findAllByIsmanagedAndDeletedAtIsNull(boolean ismanaged, Pageable pageable);
}
