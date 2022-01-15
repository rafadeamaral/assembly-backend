package com.amaral.assembly.minute.repository;

import com.amaral.assembly.minute.domain.Minute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MinuteRepository extends JpaRepository<Minute, Integer> {

    Optional<Minute> findByTitleIgnoreCase(String title);

}
