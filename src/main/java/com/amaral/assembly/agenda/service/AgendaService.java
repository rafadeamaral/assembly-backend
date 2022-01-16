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

        findById(obj.getId());

        validateTitle(obj);

        return save(obj);
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

        if (!isNull(votingDTO.getMinutes())) {

            if (votingDTO.getMinutes() > 0) {

                obj.setFinalVoting(LocalDateTime.now().plusMinutes(votingDTO.getMinutes()));

            } else {

                throw new ServiceException("invalid.voting.time");
            }

        } else {

            obj.setFinalVoting(LocalDateTime.now().plusMinutes(1));
        }

        obj.setStatus(AgendaStatus.ON_VOTING);

        update(obj);
    }

    public void closeVoting() {

        repository.findByFinalVoting(AgendaStatus.ON_VOTING, LocalDateTime.now()).forEach(entity -> {

            entity.setStatus(AgendaStatus.CLOSED);

            AgendaDTO obj = mapper.map(entity, AgendaDTO.class);

            update(obj);
        });
    }

    public void vote(VoteDTO voteDTO) {

        AgendaDTO obj = findById(voteDTO.getAgendaId());

        if (!AgendaStatus.ON_VOTING.equals(obj.getStatus())) {

            throw new ServiceException("agenda.not.open.voting");

        } else {

            voteService.create(voteDTO);
        }
    }
}
