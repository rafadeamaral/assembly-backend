package com.amaral.assemblyproducer.message.endpoint;

import com.amaral.assemblyproducer.message.domain.Message;
import com.amaral.assemblyproducer.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/message")
public class MessageEndpoint {

    @Autowired
    private MessageService service;

    @PostMapping
    public ResponseEntity send(@RequestBody Message body) {

        service.send(body);

        return ResponseEntity.noContent().build();
    }

}
