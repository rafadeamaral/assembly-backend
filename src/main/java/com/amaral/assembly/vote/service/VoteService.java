package com.amaral.assembly.vote.service;

import com.amaral.assembly.associate.domain.AssociateDTO;
import com.amaral.assembly.associate.service.AssociateService;
import com.amaral.assembly.common.exception.DataIntegratyViolationException;
import com.amaral.assembly.vote.domain.Vote;
import com.amaral.assembly.vote.domain.VoteDTO;
import com.amaral.assembly.vote.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VoteRepository repository;

    @Autowired
    private AssociateService associateService;

    @Autowired
    private ModelMapper mapper;

    public void create(VoteDTO obj) {

        AssociateDTO associateDTO = associateService.findById(obj.getAssociateId());

        associateService.validateVote(associateDTO);

        Vote entity = mapper.map(obj, Vote.class);

        Optional<Vote> optional = repository.findById(entity.getId());

        if (optional.isPresent()) {

            throw new DataIntegratyViolationException("vote.already.registered");

        } else {

            repository.save(entity);
        }
    }

}
