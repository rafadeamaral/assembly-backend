package com.amaral.assembly.agenda.service;

import com.amaral.assembly.agenda.domain.Agenda;
import com.amaral.assembly.agenda.domain.AgendaDTO;
import com.amaral.assembly.agenda.domain.AgendaStatus;
import com.amaral.assembly.agenda.repository.AgendaRepository;
import com.amaral.assembly.common.exception.DataIntegratyViolationException;
import com.amaral.assembly.common.exception.ObjectNotFoundException;
import com.amaral.assembly.common.exception.ServiceException;
import com.amaral.assembly.event.domain.Event;
import com.amaral.assembly.event.domain.EventDTO;
import com.amaral.assembly.event.service.EventService;
import com.amaral.assembly.vote.domain.VoteDTO;
import com.amaral.assembly.vote.domain.VotingDTO;
import com.amaral.assembly.vote.domain.VotingResultDTO;
import com.amaral.assembly.vote.domain.VotoAnswer;
import com.amaral.assembly.vote.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AgendaServiceTest {

    private static final int INDEX = 0;

    private static final Integer ID = 1;

    private static final String TITLE = "Test title";

    private static final String DESCRIPTION = "Teste description";

    private static final Long MINUTES = 1L;

    private static final Long AMOUNT_TOTAL = 3L;

    @InjectMocks
    private AgendaService service;

    @Mock
    private AgendaRepository repository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private EventService eventService;

    @Mock
    private VoteService voteService;

    private Agenda agenda;

    private AgendaDTO agendaDTO;

    private Optional<Agenda> optional;

    private VotingDTO votingDTO;

    private EventDTO eventDTO;

    private VoteDTO voteDTO;

    private VotingResultDTO votingResultDTO;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        startAgenda();
    }

    private void startAgenda() {

        Event event = new Event();
        event.setId(ID);

        agenda = new Agenda();
        agenda.setId(ID);
        agenda.setTitle(TITLE);
        agenda.setDescription(DESCRIPTION);
        agenda.setStatus(AgendaStatus.OPEN);
        agenda.setEvent(event);

        optional = Optional.of(agenda);

        agendaDTO = new AgendaDTO();
        agendaDTO.setId(ID);
        agendaDTO.setTitle(TITLE);
        agendaDTO.setDescription(DESCRIPTION);
        agendaDTO.setStatus(AgendaStatus.OPEN);
        agendaDTO.setEventId(ID);

        votingDTO = new VotingDTO();
        votingDTO.setAgendaId(ID);
        votingDTO.setMinutes(MINUTES);

        eventDTO = new EventDTO();
        eventDTO.setId(ID);
        eventDTO.setTitle(TITLE);
        eventDTO.setDate(LocalDate.now());

        voteDTO = new VoteDTO();
        voteDTO.setAgendaId(ID);
        voteDTO.setAssociateId(ID);
        voteDTO.setAnswer(VotoAnswer.YES);

        votingResultDTO = new VotingResultDTO(ID, 1L, 2L, AMOUNT_TOTAL);
    }

    private void assertObject(AgendaDTO obj) {

        assertEquals(ID, obj.getId());
        assertEquals(TITLE, obj.getTitle());
        assertEquals(DESCRIPTION, obj.getDescription());
        assertEquals(AgendaStatus.OPEN, obj.getStatus());
        assertEquals(ID, obj.getEventId());
    }

    @Test
    void whenFindAllThenReturnAnListOfAgendaDTO() {

        when(repository.findAll()).thenReturn(List.of(agenda));
        when(mapper.map(any(), any())).thenReturn(agendaDTO);

        List<AgendaDTO> response = service.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(AgendaDTO.class, response.get(INDEX).getClass());

        assertObject(response.get(INDEX));
    }

    @Test
    void whenFindByIdThenReturnAnAgendaDTO() {

        when(repository.findById(anyInt())).thenReturn(optional);
        when(mapper.map(any(), any())).thenReturn(agendaDTO);

        AgendaDTO response = service.findById(ID);

        assertNotNull(response);
        assertEquals(AgendaDTO.class, response.getClass());

        assertObject(response);
    }

    @Test
    void whenFindByIdThenReturnObjectNotFoundException() {

        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        try {

            service.findById(ID);

            fail("Must throw exception");

        } catch (Exception ex) {

            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals("agenda.not.found", ex.getMessage());
        }
    }

    @Test
    void whenCreateThenReturnAnAgendaDTO() {

        when(repository.save(any())).thenReturn(agenda);
        when(mapper.map(any(Agenda.class), any())).thenReturn(agendaDTO);

        AgendaDTO response = service.create(agendaDTO);

        assertNotNull(response);
        assertEquals(AgendaDTO.class, response.getClass());

        assertObject(response);
    }

    @Test
    void whenCreateThenReturnDataIntegrityViolationException() {

        when(repository.findByTitleIgnoreCase(anyString())).thenReturn(optional);

        try {

            agendaDTO.setId(2);

            service.create(agendaDTO);

            fail("Must throw exception");

        } catch (Exception ex) {

            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals("title.already.registered", ex.getMessage());
        }
    }

    @Test
    void whenUpdateThenReturnAnAgendaDTO() {

        when(repository.findById(anyInt())).thenReturn(optional);
        when(repository.save(any())).thenReturn(agenda);
        when(mapper.map(any(Agenda.class), any())).thenReturn(agendaDTO);

        AgendaDTO response = service.update(agendaDTO);

        assertNotNull(response);
        assertEquals(AgendaDTO.class, response.getClass());

        assertObject(response);
    }

    @Test
    void whenUpdateThenReturnDataIntegrityViolationException() {

        when(repository.findById(anyInt())).thenReturn(optional);
        when(mapper.map(any(Agenda.class), any())).thenReturn(agendaDTO);
        when(repository.findByTitleIgnoreCase(anyString())).thenReturn(optional);

        try {

            agendaDTO.setId(2);

            service.update(agendaDTO);

            fail("Must throw exception");

        } catch (Exception ex) {

            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals("title.already.registered", ex.getMessage());
        }
    }

    @Test
    void whenVotingThenRunSuccess() {

        when(repository.findById(anyInt())).thenReturn(optional);
        when(mapper.map(any(Agenda.class), any())).thenReturn(agendaDTO);
        when(eventService.findById(anyInt())).thenReturn(eventDTO);

        service.voting(votingDTO);

        verify(repository, times(1)).findById(anyInt());
    }

    @Test
    void whenFindVotingResultByAgendaThenReturnAVotingResultDTO() {

        agendaDTO.setStatus(AgendaStatus.CLOSED);

        when(repository.findById(anyInt())).thenReturn(optional);
        when(mapper.map(any(), any())).thenReturn(agendaDTO);
        when(voteService.findVotingResultByAgenda(anyInt())).thenReturn(votingResultDTO);

        VotingResultDTO response = service.findVotingResultByAgenda(ID);

        assertNotNull(response);
        assertEquals(VotingResultDTO.class, response.getClass());

        assertEquals(ID, response.getAgendaId());
        assertEquals(AMOUNT_TOTAL, response.getAmountTotal());
    }

    @Test
    void whenFindVotingResultByAgendaThenReturnServiceException() {

        when(repository.findById(anyInt())).thenReturn(optional);
        when(mapper.map(any(), any())).thenReturn(agendaDTO);

        try {

            service.findVotingResultByAgenda(ID);

            fail("Must throw exception");

        } catch (Exception ex) {

            assertEquals(ServiceException.class, ex.getClass());
            assertEquals("agenda.voting.not.closed.or.not.started", ex.getMessage());
        }
    }

    @Test
    void whenVoteThenRunSuccess() {

        agendaDTO.setStatus(AgendaStatus.ON_VOTING);

        when(repository.findById(anyInt())).thenReturn(optional);
        when(mapper.map(any(Agenda.class), any())).thenReturn(agendaDTO);

        service.vote(voteDTO);

        verify(repository, times(1)).findById(anyInt());
    }

}
