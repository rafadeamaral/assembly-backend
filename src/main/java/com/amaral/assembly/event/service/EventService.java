package com.amaral.assembly.event.service;

import com.amaral.assembly.common.exception.ObjectNotFoundException;
import com.amaral.assembly.event.domain.Event;
import com.amaral.assembly.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    public List<Event> findAll() {
        return repository.findAll();
    }

    public Event findById(Integer id) {

        Optional<Event> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException("object.not.found"));
    }

    public Event create(Event obj) {
        return repository.save(obj);
    }

    public Event update(Event obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

}
