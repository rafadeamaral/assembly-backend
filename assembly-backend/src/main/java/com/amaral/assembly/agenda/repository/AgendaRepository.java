package com.amaral.assembly.agenda.repository;

import com.amaral.assembly.agenda.domain.Agenda;
import com.amaral.assembly.agenda.domain.AgendaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

    Optional<Agenda> findByTitleIgnoreCase(String title);

    @Query(value = "from Agenda where status = ?1 and finalVoting <= ?2 order by finalVoting")
    List<Agenda> findByFinalVoting(AgendaStatus status, LocalDateTime finalVote);

}
