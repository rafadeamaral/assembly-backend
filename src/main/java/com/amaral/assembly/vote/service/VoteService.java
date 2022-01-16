package com.amaral.assembly.vote.service;

import com.amaral.assembly.associate.service.AssociateService;
import com.amaral.assembly.common.exception.DataIntegratyViolationException;
import com.amaral.assembly.vote.domain.Vote;
import com.amaral.assembly.vote.domain.VoteDTO;
import com.amaral.assembly.vote.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class VoteService {

    @Autowired
    private VoteRepository repository;

    @Autowired
    private AssociateService associateService;

    @Autowired
    private ModelMapper mapper;

    public void create(VoteDTO obj) {

        associateService.validateVote(obj.getAssociateId());

        Vote entity = mapper.map(obj, Vote.class);

        validateVote(entity);

        repository.save(entity);

        log.debug("Vote { agendaId = " + obj.getAgendaId() + ", associateId = " + obj.getAssociateId()
                + ", answer = " + obj.getAnswer() + " }");
    }

    private void validateVote(Vote entity) {

        Optional<Vote> optional = repository.findById(entity.getId());

        if (optional.isPresent()) {

            throw new DataIntegratyViolationException("vote.already.registered");
        }
    }

}
