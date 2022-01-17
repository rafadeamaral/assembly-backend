package com.amaral.assembly.agenda.scheduler;

import com.amaral.assembly.agenda.service.AgendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class AgendaSchedulerTest {

    @InjectMocks
    private AgendaScheduler scheduler;

    @Mock
    private AgendaService service;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCloseVotingThenRunSuccess() {

        doNothing().when(service).closeVoting();

        scheduler.closeVoting();

        verify(service, times(1)).closeVoting();
    }

}
