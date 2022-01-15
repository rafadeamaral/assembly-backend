package com.amaral.assembly.event.endpoint;

import com.amaral.assembly.event.domain.Event;
import com.amaral.assembly.event.domain.EventDTO;
import com.amaral.assembly.event.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/event")
public class EventEndpoint {

    private static final String ID = "/{id}";

    @Autowired
    private EventService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<EventDTO>> findAll() {

        List<EventDTO> body = service.findAll()
                .stream().map(obj -> mapper.map(obj, EventDTO.class)).collect(Collectors.toList());

        return ResponseEntity.ok(body);
    }

    @GetMapping(value = ID)
    public ResponseEntity<EventDTO> findById(@PathVariable Integer id) {

        EventDTO body = mapper.map(service.findById(id), EventDTO.class);

        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<EventDTO> create(@RequestBody EventDTO obj) {

        Event event = service.create(mapper.map(obj, Event.class));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(ID).buildAndExpand(event.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = ID)
    public ResponseEntity<EventDTO> update(@PathVariable Integer id, @RequestBody EventDTO obj) {

        obj.setId(id);

        Event event = service.update(mapper.map(obj, Event.class));

        EventDTO body = mapper.map(event, EventDTO.class);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping(value = ID)
    public ResponseEntity<EventDTO> delete(@PathVariable Integer id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
