package com.amaral.assembly.agenda.scheduler;

import com.amaral.assembly.agenda.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AgendaScheduler {

    @Autowired
    private AgendaService service;

    @Scheduled(fixedDelayString = "${agenda.scheduler.delay}")
    public void closeVoting() {

        service.closeVoting();
    }

}
