package com.amaral.assemblyproducer.message.service;

import com.amaral.assemblyproducer.message.domain.Message;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key}")
    private String queue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Message message) {

        try {

            rabbitTemplate.convertAndSend(exchange, queue, message);

        } catch (Exception ex) {

            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }

}
