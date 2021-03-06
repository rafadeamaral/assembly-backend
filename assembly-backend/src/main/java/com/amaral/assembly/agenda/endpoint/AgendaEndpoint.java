package com.amaral.assembly.agenda.endpoint;

import com.amaral.assembly.agenda.domain.AgendaDTO;
import com.amaral.assembly.agenda.service.AgendaService;
import com.amaral.assembly.vote.domain.VoteDTO;
import com.amaral.assembly.vote.domain.VotingDTO;
import com.amaral.assembly.vote.domain.VotingResultDTO;
import io.swagger.annotations.Api;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Api(tags = "Agenda")
@RestController
@RequestMapping(value = "/agenda")
public class AgendaEndpoint {

    private static final String ID = "/{id}";

    @Autowired
    private AgendaService service;

    @GetMapping
    public ResponseEntity<List<AgendaDTO>> findAll() {

        List<AgendaDTO> body = service.findAll();

        return ResponseEntity.ok(body);
    }

    @GetMapping(value = ID)
    public ResponseEntity<AgendaDTO> findById(@PathVariable Integer id) {

        AgendaDTO body = service.findById(id);

        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<AgendaDTO> create(@Valid @RequestBody AgendaDTO body) {

        AgendaDTO obj = service.create(body);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(ID).buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = ID)
    public ResponseEntity<AgendaDTO> update(@PathVariable Integer id, @Valid @RequestBody AgendaDTO body) {

        body.setId(id);

        body = service.update(body);

        return ResponseEntity.ok(body);
    }

    @PostMapping(value = ID + "/voting")
    public ResponseEntity<VotingDTO> voting(@PathVariable Integer id, @RequestBody VotingDTO body) {

        body.setAgendaId(id);

        service.voting(body);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = ID + "/voting/result")
    public ResponseEntity<VotingResultDTO> findVotingResult(@PathVariable Integer id) {

        VotingResultDTO body = service.findVotingResultByAgenda(id);

        return ResponseEntity.ok(body);
    }

    @PostMapping(value = ID + "/vote")
    public ResponseEntity<VoteDTO> vote(@PathVariable Integer id, @Valid @RequestBody VoteDTO body) {

        body.setAgendaId(id);

        service.vote(body);

        return ResponseEntity.noContent().build();
    }

}
