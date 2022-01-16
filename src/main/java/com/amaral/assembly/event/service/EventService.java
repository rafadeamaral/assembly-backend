package com.amaral.assembly.event.service;

import com.amaral.assembly.common.exception.DataIntegratyViolationException;
import com.amaral.assembly.common.exception.ObjectNotFoundException;
import com.amaral.assembly.event.domain.Event;
import com.amaral.assembly.event.domain.EventDTO;
import com.amaral.assembly.event.repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    @Autowired
    private ModelMapper mapper;

    public List<EventDTO> findAll() {

        return repository.findAll()
                .stream().map(obj -> mapper.map(obj, EventDTO.class)).collect(Collectors.toList());
    }

    public EventDTO findById(Integer id) {

        Optional<Event> optional = repository.findById(id);

        Event entity = optional.orElseThrow(() -> new ObjectNotFoundException("event.not.found"));

        return mapper.map(entity, EventDTO.class);
    }

    public EventDTO create(EventDTO obj) {

        validateTitle(obj);

        return save(obj);
    }

    public EventDTO update(EventDTO obj) {

        findById(obj.getId());

        validateTitle(obj);

        return save(obj);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    private EventDTO save(EventDTO obj) {

        Event entity = mapper.map(obj, Event.class);

        entity = repository.save(entity);

        return mapper.map(entity, EventDTO.class);
    }

    private void validateTitle(EventDTO obj) {

        Optional<Event> optional = repository.findByTitleIgnoreCase(obj.getTitle());

        if (optional.isPresent() && !optional.get().getId().equals(obj.getId())) {

            throw new DataIntegratyViolationException("title.already.registered");
        }
    }

}
