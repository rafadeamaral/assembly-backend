package com.amaral.assembly.event.repository;

import com.amaral.assembly.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    Optional<Event> findByTitleIgnoreCase(String title);

}
