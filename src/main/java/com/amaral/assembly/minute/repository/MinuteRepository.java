package com.amaral.assembly.minute.repository;

import com.amaral.assembly.minute.domain.Minute;
import com.amaral.assembly.minute.domain.MinuteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MinuteRepository extends JpaRepository<Minute, Integer> {

    Optional<Minute> findByTitleIgnoreCase(String title);

    @Query(value = "from Minute where status = ?1 and finalVoting <= ?2 order by finalVoting")
    List<Minute> findByFinalVoting(MinuteStatus status, LocalDateTime finalVote);

}
