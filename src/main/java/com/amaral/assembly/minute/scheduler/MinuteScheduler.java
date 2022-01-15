package com.amaral.assembly.minute.scheduler;

import com.amaral.assembly.minute.service.MinuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MinuteScheduler {

    @Autowired
    private MinuteService service;

    @Scheduled(fixedDelayString = "${minute.scheduler.delay}")
    public void closeVoting() {

        service.closeVoting();
    }

}
