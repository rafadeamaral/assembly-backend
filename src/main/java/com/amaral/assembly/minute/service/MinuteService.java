package com.amaral.assembly.minute.service;

import com.amaral.assembly.common.exception.DataIntegratyViolationException;
import com.amaral.assembly.common.exception.ObjectNotFoundException;
import com.amaral.assembly.event.domain.Event;
import com.amaral.assembly.event.service.EventService;
import com.amaral.assembly.minute.domain.Minute;
import com.amaral.assembly.minute.domain.MinuteDTO;
import com.amaral.assembly.minute.domain.MinuteStatus;
import com.amaral.assembly.minute.repository.MinuteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Event event = eventService.findById(obj.getEventId());

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

}
