package com.springboot.obbm.respository;

import com.springboot.obbm.model.PerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerGroupRepository extends JpaRepository<PerGroup, String> {
}
