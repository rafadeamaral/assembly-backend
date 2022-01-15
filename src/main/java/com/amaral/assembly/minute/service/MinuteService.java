package com.amaral.assembly.minute.service;

import com.amaral.assembly.common.exception.DataIntegratyViolationException;
import com.amaral.assembly.common.exception.ObjectNotFoundException;
import com.amaral.assembly.common.exception.ServiceException;
import com.amaral.assembly.event.domain.Event;
import com.amaral.assembly.event.domain.EventDTO;
import com.amaral.assembly.event.service.EventService;
import com.amaral.assembly.minute.domain.Minute;
import com.amaral.assembly.minute.domain.MinuteDTO;
import com.amaral.assembly.minute.domain.MinuteStatus;
import com.amaral.assembly.minute.domain.VotingDTO;
import com.amaral.assembly.minute.repository.MinuteRepository;
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

        validateByTitle(obj);

        obj.setStatus(MinuteStatus.OPEN);

        return save(obj);
    }

    public MinuteDTO update(MinuteDTO obj) {

        findById(obj.getId());

        validateByTitle(obj);

        return save(obj);
    }

    private MinuteDTO save(MinuteDTO obj) {

        EventDTO eventDTO = eventService.findById(obj.getEventId());

        Event event = new Event();
        event.setId(eventDTO.getId());

        Minute entity = mapper.map(obj, Minute.class);
        entity.setEvent(event);

        entity = repository.save(entity);

        return mapper.map(entity, MinuteDTO.class);
    }

    private void validateByTitle(MinuteDTO obj) {

        Optional<Minute> optional = repository.findByTitleIgnoreCase(obj.getTitle());

        if (optional.isPresent() && !optional.get().getId().equals(obj.getId())) {

            throw new DataIntegratyViolationException("title.already.registered");
        }
    }

    public MinuteDTO voting(VotingDTO voteDTO) {

        MinuteDTO obj = findById(voteDTO.getId());

        if (!isNull(voteDTO.getTimeInMinutes())) {

            if (voteDTO.getTimeInMinutes() > 0) {

                obj.setFinalVoting(LocalDateTime.now().plusMinutes(voteDTO.getTimeInMinutes()));

            } else {

                throw new ServiceException("invalid.voting.time");
            }

        } else {

            obj.setFinalVoting(LocalDateTime.now().plusMinutes(1));
        }

        obj.setStatus(MinuteStatus.ON_VOTING);

        obj = update(obj);

        return obj;
    }

    public void closeVoting() {

        repository.findByFinalVoting(MinuteStatus.ON_VOTING, LocalDateTime.now()).forEach(minute -> {

            minute.setStatus(MinuteStatus.CLOSED);

            MinuteDTO obj = mapper.map(minute, MinuteDTO.class);

            update(obj);
        });
    }

}
