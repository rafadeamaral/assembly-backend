package com.amaral.assembly.agenda.service;

import com.amaral.assembly.agenda.domain.Agenda;
import com.amaral.assembly.agenda.domain.AgendaDTO;
import com.amaral.assembly.agenda.domain.AgendaStatus;
import com.amaral.assembly.agenda.repository.AgendaRepository;
import com.amaral.assembly.common.exception.DataIntegratyViolationException;
import com.amaral.assembly.common.exception.ObjectNotFoundException;
import com.amaral.assembly.common.exception.ServiceException;
import com.amaral.assembly.event.domain.EventDTO;
import com.amaral.assembly.event.service.EventService;
import com.amaral.assembly.vote.domain.VoteDTO;
import com.amaral.assembly.vote.domain.VotingDTO;
import com.amaral.assembly.vote.domain.VotingResultDTO;
import com.amaral.assembly.vote.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class AgendaService {

    @Value("${rabbitmq.endpoint}")
    private String endpoint;

    @Autowired
    private AgendaRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EventService eventService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private RestTemplate restTemplate;

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

            log.debug("validateStatus - Agenda { id = " + dto.getId() + ", status = "
                    + dto.getStatus() + ", newStatus = " + obj.getStatus() + " }");

            if (!AgendaStatus.CANCELED.equals(obj.getStatus()) && !AgendaStatus.OPEN.equals(obj.getStatus())) {

                throw new ServiceException("status.must.be.canceled.or.opened");
            }

            if (!AgendaStatus.CANCELED.equals(dto.getStatus()) && !AgendaStatus.OPEN.equals(dto.getStatus())) {

                throw new ServiceException("status.changing.is.not.allowed");
            }

        } else {

            obj.setStatus(dto.getStatus());
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

        AgendaDTO obj = findById(votingDTO.getAgendaId());

        validateVotingStatus(obj);

        validateEvent(obj);

        if (!isNull(votingDTO.getMinutes())) {

            validateVotingMinutes(votingDTO);

            obj.setFinalVoting(LocalDateTime.now().plusMinutes(votingDTO.getMinutes()));

            log.debug("voting - Agenda { id = " + obj.getId() + ", minutes = " + votingDTO.getMinutes() + " }");

        } else {

            obj.setFinalVoting(LocalDateTime.now().plusMinutes(1));

            log.debug("voting - Agenda { id = " + obj.getId() + ", minutes = 1 }");
        }

        obj.setStatus(AgendaStatus.ON_VOTING);

        save(obj);
    }

    private void validateEvent(AgendaDTO obj) {

        EventDTO eventDTO = eventService.findById(obj.getEventId());

        if (LocalDate.now().compareTo(eventDTO.getDate()) < 0) {

            throw new ServiceException("invalid.event.date");
        }
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

            log.debug("closeVoting - Agenda { id = " + entity.getId() + ", eventID = "
                    + entity.getEvent().getId() + " }");

            entity.setStatus(AgendaStatus.CLOSED);

            AgendaDTO obj = mapper.map(entity, AgendaDTO.class);

            save(obj);

            sendVotingResult(obj.getId());
        });
    }

    private void sendVotingResult(Integer agendaId) {

        try {

            VotingResultDTO request = findVotingResultByAgenda(agendaId);

            restTemplate.postForEntity(endpoint, request, String.class);

        } catch (Exception e) {

            log.error("sendVotingResult", e);
        }
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

    public VotingResultDTO findVotingResultByAgenda(Integer agendaId) {

        AgendaDTO obj = findById(agendaId);

        validateVotingResultStatus(obj);

        return voteService.findVotingResultByAgenda(agendaId);
    }

    private void validateVotingResultStatus(AgendaDTO obj) {

        if (!AgendaStatus.CLOSED.equals(obj.getStatus())) {

            throw new ServiceException("agenda.voting.not.closed.or.not.started");
        }
    }

}
