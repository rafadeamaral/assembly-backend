package com.amaral.assembly.associate.endpoint;

import com.amaral.assembly.associate.domain.AssociateDTO;
import com.amaral.assembly.associate.service.AssociateService;
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

@RestController
@RequestMapping(value = "/associate")
public class AssociateEndpoint {

    private static final String ID = "/{id}";

    @Autowired
    private AssociateService service;

    @GetMapping
    public ResponseEntity<List<AssociateDTO>> findAll() {

        List<AssociateDTO> body = service.findAll();

        return ResponseEntity.ok(body);
    }

    @GetMapping(value = ID)
    public ResponseEntity<AssociateDTO> findById(@PathVariable Integer id) {

        AssociateDTO body = service.findById(id);

        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<AssociateDTO> create(@Valid @RequestBody AssociateDTO body) {

        AssociateDTO obj = service.create(body);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(ID).buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = ID)
    public ResponseEntity<AssociateDTO> update(@PathVariable Integer id, @Valid @RequestBody AssociateDTO body) {

        body.setId(id);

        body = service.update(body);

        return ResponseEntity.ok(body);
    }

}
