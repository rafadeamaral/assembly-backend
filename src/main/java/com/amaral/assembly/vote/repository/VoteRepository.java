package com.amaral.assembly.vote.repository;

import com.amaral.assembly.vote.domain.Vote;
import com.amaral.assembly.vote.domain.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {

    @Query(value = "from Vote where voteId.agenda.id = ?1")
    List<Vote> findByAgendaId(Integer agendaId);

}
