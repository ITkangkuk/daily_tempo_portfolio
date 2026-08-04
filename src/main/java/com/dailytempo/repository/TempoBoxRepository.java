package com.dailytempo.repository;

import com.dailytempo.domain.TempoBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TempoBoxRepository extends JpaRepository<TempoBox, Long> {
    List<TempoBox> findAllByUserUsernameOrderByCreatedAtAsc(String username);
    Optional<TempoBox> findByIdAndUserUsername(Long id, String username);
}
