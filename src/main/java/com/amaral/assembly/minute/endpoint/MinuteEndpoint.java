package com.amaral.assembly.minute.endpoint;

import com.amaral.assembly.minute.domain.MinuteDTO;
import com.amaral.assembly.minute.domain.VotingDTO;
import com.amaral.assembly.minute.service.MinuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(value = "/minute")
public class MinuteEndpoint {

    private static final String ID = "/{id}";

    @Autowired
    private MinuteService service;

    @GetMapping
    public ResponseEntity<List<MinuteDTO>> findAll() {

        List<MinuteDTO> body = service.findAll();

        return ResponseEntity.ok(body);
    }

    @GetMapping(value = ID)
    public ResponseEntity<MinuteDTO> findById(@PathVariable Integer id) {

        MinuteDTO body = service.findById(id);

        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<MinuteDTO> create(@RequestBody MinuteDTO body) {

        MinuteDTO obj = service.create(body);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(ID).buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = ID)
    public ResponseEntity<MinuteDTO> update(@PathVariable Integer id, @RequestBody MinuteDTO body) {

        body.setId(id);

        body = service.update(body);

        return ResponseEntity.ok(body);
    }

    @PostMapping(value = ID + "/voting")
    public ResponseEntity<MinuteDTO> voting(@PathVariable Integer id, @RequestBody VotingDTO body) {

        body.setId(id);

        MinuteDTO response = service.voting(body);

        return ResponseEntity.ok(response);
    }

}
