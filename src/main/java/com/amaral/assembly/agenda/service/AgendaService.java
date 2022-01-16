package com.amaral.assembly.agenda.service;

import com.amaral.assembly.agenda.domain.Agenda;
import com.amaral.assembly.agenda.domain.AgendaDTO;
import com.amaral.assembly.agenda.domain.AgendaStatus;
import com.amaral.assembly.agenda.repository.AgendaRepository;
import com.amaral.assembly.common.exception.DataIntegratyViolationException;
import com.amaral.assembly.common.exception.ObjectNotFoundException;
import com.amaral.assembly.common.exception.ServiceException;
import com.amaral.assembly.event.service.EventService;
import com.amaral.assembly.vote.domain.VoteDTO;
import com.amaral.assembly.vote.domain.VotingDTO;
import com.amaral.assembly.vote.service.VoteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EventService eventService;

    @Autowired
    private VoteService voteService;

    public List<AgendaDTO> findAll() {

        return repository.findAll()
                .stream().map(obj -> mapper.map(obj, AgendaDTO.class)).collect(Collectors.toList());
    }

    public AgendaDTO findById(Integer id) {

        Optional<Agenda> optional = repository.findById(id);

        Agenda entity = optional.orElseThrow(() -> new ObjectNotFoundException("agenda.not.found"));

        return mapper.map(entity, AgendaDTO.class);
    }

    public AgendaDTO create(AgendaDTO obj) {

        validateTitle(obj);

        obj.setStatus(AgendaStatus.OPEN);

        return save(obj);
    }

    public AgendaDTO update(AgendaDTO obj) {

        AgendaDTO dto = findById(obj.getId());

        validateStatus(obj, dto);

        validateTitle(obj);

        return save(obj);
    }

    private void validateStatus(AgendaDTO obj, AgendaDTO dto) {

        if (!isNull(obj.getStatus())) {

            if (!AgendaStatus.CANCELED.equals(obj.getStatus())) {

                throw new ServiceException("status.must.be.canceled.only");
            }

            if (!AgendaStatus.OPEN.equals(dto.getStatus())) {

                throw new ServiceException("only.allowed.cancel.open.agenda");
            }
        }
    }

    private AgendaDTO save(AgendaDTO obj) {

        eventService.findById(obj.getEventId());

        Agenda entity = mapper.map(obj, Agenda.class);

        entity = repository.save(entity);

        return mapper.map(entity, AgendaDTO.class);
    }

    private void validateTitle(AgendaDTO obj) {

        Optional<Agenda> optional = repository.findByTitleIgnoreCase(obj.getTitle());

        if (optional.isPresent() && !optional.get().getId().equals(obj.getId())) {

            throw new DataIntegratyViolationException("title.already.registered");
        }
    }

    public void voting(VotingDTO votingDTO) {

        AgendaDTO obj = findById(votingDTO.getId());

        validateVotingStatus(obj);

        if (!isNull(votingDTO.getMinutes())) {

            validateVotingMinutes(votingDTO);

            obj.setFinalVoting(LocalDateTime.now().plusMinutes(votingDTO.getMinutes()));

        } else {

            obj.setFinalVoting(LocalDateTime.now().plusMinutes(1));
        }

        obj.setStatus(AgendaStatus.ON_VOTING);

        save(obj);
    }

    private void validateVotingMinutes(VotingDTO votingDTO) {

        if (votingDTO.getMinutes() < 1) {

            throw new ServiceException("invalid.voting.time");
        }
    }

    private void validateVotingStatus(AgendaDTO obj) {

        if (!AgendaStatus.OPEN.equals(obj.getStatus())) {

            throw new ServiceException("only.allowed.open.voting.open.agenda");
        }
    }

    public void closeVoting() {

        repository.findByFinalVoting(AgendaStatus.ON_VOTING, LocalDateTime.now()).forEach(entity -> {

            entity.setStatus(AgendaStatus.CLOSED);

            AgendaDTO obj = mapper.map(entity, AgendaDTO.class);

            save(obj);
        });
    }

    public void vote(VoteDTO voteDTO) {

        AgendaDTO obj = findById(voteDTO.getAgendaId());

        validateVoteStatus(obj);

        voteService.create(voteDTO);
    }

    private void validateVoteStatus(AgendaDTO obj) {

        if (!AgendaStatus.ON_VOTING.equals(obj.getStatus())) {

            throw new ServiceException("agenda.not.open.voting");
        }
    }

}
