package com.amaral.assemblyconsumer.message.service;

import com.amaral.assemblyconsumer.message.domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {

    @RabbitListener(queues = "${rabbitmq.routing-key}")
    public void consumer(Message message) {

        try {

            log.info("###########################");
            log.info("Agenda ID..: " + message.getAgendaId());
            log.info("Total Votes: " + message.getAmountTotal());
            log.info("Votes Yes..: " + message.getAmountYes());
            log.info("Votes No...: " + message.getAmountNo());
            log.info("###########################");

        } catch (Exception ex) {

            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }

}
