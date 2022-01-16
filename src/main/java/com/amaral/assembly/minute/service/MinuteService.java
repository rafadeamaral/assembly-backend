package com.amaral.assembly.minute.service;

import com.amaral.assembly.common.exception.DataIntegratyViolationException;
import com.amaral.assembly.common.exception.ObjectNotFoundException;
import com.amaral.assembly.common.exception.ServiceException;
import com.amaral.assembly.event.service.EventService;
import com.amaral.assembly.minute.domain.Minute;
import com.amaral.assembly.minute.domain.MinuteDTO;
import com.amaral.assembly.minute.domain.MinuteStatus;
import com.amaral.assembly.minute.repository.MinuteRepository;
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
public class MinuteService {

    @Autowired
    private MinuteRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EventService eventService;

    @Autowired
    private VoteService voteService;

    public List<MinuteDTO> findAll() {

        return repository.findAll()
                .stream().map(obj -> mapper.map(obj, MinuteDTO.class)).collect(Collectors.toList());
    }

    public MinuteDTO findById(Integer id) {

        Optional<Minute> optional = repository.findById(id);

        Minute entity = optional.orElseThrow(() -> new ObjectNotFoundException("minute.not.found"));

        return mapper.map(entity, MinuteDTO.class);
    }

    public MinuteDTO create(MinuteDTO obj) {

        validateTitle(obj);

        obj.setStatus(MinuteStatus.OPEN);

        return save(obj);
    }

    public MinuteDTO update(MinuteDTO obj) {

        findById(obj.getId());

        validateTitle(obj);

        return save(obj);
    }

    private MinuteDTO save(MinuteDTO obj) {

        eventService.findById(obj.getEventId());

        Minute entity = mapper.map(obj, Minute.class);

        entity = repository.save(entity);

        return mapper.map(entity, MinuteDTO.class);
    }

    private void validateTitle(MinuteDTO obj) {

        Optional<Minute> optional = repository.findByTitleIgnoreCase(obj.getTitle());

        if (optional.isPresent() && !optional.get().getId().equals(obj.getId())) {

            throw new DataIntegratyViolationException("title.already.registered");
        }
    }

    public void voting(VotingDTO votingDTO) {

        MinuteDTO obj = findById(votingDTO.getId());

        if (!isNull(votingDTO.getTimeInMinutes())) {

            if (votingDTO.getTimeInMinutes() > 0) {

                obj.setFinalVoting(LocalDateTime.now().plusMinutes(votingDTO.getTimeInMinutes()));

            } else {

                throw new ServiceException("invalid.voting.time");
            }

        } else {

            obj.setFinalVoting(LocalDateTime.now().plusMinutes(1));
        }

        obj.setStatus(MinuteStatus.ON_VOTING);

        update(obj);
    }

    public void closeVoting() {

        repository.findByFinalVoting(MinuteStatus.ON_VOTING, LocalDateTime.now()).forEach(minute -> {

            minute.setStatus(MinuteStatus.CLOSED);

            MinuteDTO obj = mapper.map(minute, MinuteDTO.class);

            update(obj);
        });
    }

    public void vote(VoteDTO voteDTO) {

        MinuteDTO obj = findById(voteDTO.getMinuteId());

        if (!MinuteStatus.ON_VOTING.equals(obj.getStatus())) {

            throw new ServiceException("minute.not.open.voting");

        } else {

            voteService.create(voteDTO);
        }
    }
}
