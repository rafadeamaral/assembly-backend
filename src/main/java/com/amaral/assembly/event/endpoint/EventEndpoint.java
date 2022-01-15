package com.amaral.assembly.event.endpoint;

import com.amaral.assembly.event.domain.EventDTO;
import com.amaral.assembly.event.service.EventService;
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

@RestController
@RequestMapping(value = "/event")
public class EventEndpoint {

    private static final String ID = "/{id}";

    @Autowired
    private EventService service;

    @GetMapping
    public ResponseEntity<List<EventDTO>> findAll() {

        List<EventDTO> body = service.findAll();

        return ResponseEntity.ok(body);
    }

    @GetMapping(value = ID)
    public ResponseEntity<EventDTO> findById(@PathVariable Integer id) {

        EventDTO body = service.findById(id);

        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<EventDTO> create(@RequestBody EventDTO body) {

        EventDTO obj = service.create(body);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(ID).buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = ID)
    public ResponseEntity<EventDTO> update(@PathVariable Integer id, @RequestBody EventDTO obj) {

        obj.setId(id);

        EventDTO body = service.update(obj);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping(value = ID)
    public ResponseEntity<EventDTO> delete(@PathVariable Integer id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
