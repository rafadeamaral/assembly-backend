package com.amaral.assembly.vote.repository;

import com.amaral.assembly.vote.domain.Vote;
import com.amaral.assembly.vote.domain.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {

}
