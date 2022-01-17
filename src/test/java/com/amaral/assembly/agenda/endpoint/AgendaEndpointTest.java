package com.amaral.assembly.agenda.endpoint;

import com.amaral.assembly.agenda.domain.AgendaDTO;
import com.amaral.assembly.agenda.domain.AgendaStatus;
import com.amaral.assembly.agenda.service.AgendaService;
import com.amaral.assembly.vote.domain.VoteDTO;
import com.amaral.assembly.vote.domain.VotingDTO;
import com.amaral.assembly.vote.domain.VotingResultDTO;
import com.amaral.assembly.vote.domain.VotoAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AgendaEndpointTest {

    private static final int INDEX = 0;

    private static final Integer ID = 1;

    private static final String TITLE = "Test title";

    private static final String DESCRIPTION = "Teste description";

    private static final Long MINUTES = 1L;

    private static final Long AMOUNT_TOTAL = 3L;

    @InjectMocks
    private AgendaEndpoint endpoint;

    @Mock
    private AgendaService service;

    private AgendaDTO agendaDTO;

    private VotingDTO votingDTO;

    private VoteDTO voteDTO;

    private VotingResultDTO votingResultDTO;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        startAgenda();
    }

    private void startAgenda() {

        agendaDTO = new AgendaDTO();
        agendaDTO.setId(ID);
        agendaDTO.setTitle(TITLE);
        agendaDTO.setDescription(DESCRIPTION);
        agendaDTO.setStatus(AgendaStatus.OPEN);
        agendaDTO.setEventId(ID);

        votingDTO = new VotingDTO();
        votingDTO.setAgendaId(ID);
        votingDTO.setMinutes(MINUTES);

        voteDTO = new VoteDTO();
        voteDTO.setAgendaId(ID);
        voteDTO.setAssociateId(ID);
        voteDTO.setAnswer(VotoAnswer.YES);

        votingResultDTO = new VotingResultDTO(ID, 1L, 2L, AMOUNT_TOTAL);
    }

    private void assertBody(AgendaDTO body) {

        assertEquals(ID, body.getId());
        assertEquals(TITLE, body.getTitle());
        assertEquals(DESCRIPTION, body.getDescription());
        assertEquals(AgendaStatus.OPEN, body.getStatus());
        assertEquals(ID, body.getEventId());
    }

    @Test
    void whenFindAllThenReturnAListOfAgendaDTO() {

        when(service.findAll()).thenReturn(List.of(agendaDTO));

        ResponseEntity<List<AgendaDTO>> response = endpoint.findAll();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(1, response.getBody().size());
        assertEquals(AgendaDTO.class, response.getBody().get(INDEX).getClass());

        assertBody(response.getBody().get(INDEX));
    }

    @Test
    void whenFindByIdThenReturnSuccess() {

        when(service.findById(anyInt())).thenReturn(agendaDTO);

        ResponseEntity<AgendaDTO> response = endpoint.findById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(AgendaDTO.class, response.getBody().getClass());

        assertBody(response.getBody());
    }

    @Test
    void whenCreateThenReturnCreated() {

        when(service.create(any())).thenReturn(agendaDTO);

        ResponseEntity<AgendaDTO> response = endpoint.create(agendaDTO);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().get("Location"));
    }

    @Test
    void whenUpdateThenReturnSuccess() {

        when(service.update(any())).thenReturn(agendaDTO);

        ResponseEntity<AgendaDTO> response = endpoint.update(ID, agendaDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(AgendaDTO.class, response.getBody().getClass());

        assertBody(response.getBody());
    }

    @Test
    void whenVotingThenReturnNoContent() {

        doNothing().when(service).voting(any());

        ResponseEntity<VotingDTO> response = endpoint.voting(ID, votingDTO);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).voting(any());
    }

    @Test
    void whenFindVotingResultThenReturnAVotingResultDTO() {

        when(service.findVotingResultByAgenda(anyInt())).thenReturn(votingResultDTO);

        ResponseEntity<VotingResultDTO> response = endpoint.findVotingResult(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(VotingResultDTO.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getAgendaId());
        assertEquals(AMOUNT_TOTAL, response.getBody().getAmountTotal());
    }

    @Test
    void whenVoteThenReturnNoContent() {

        doNothing().when(service).vote(any());

        ResponseEntity<VoteDTO> response = endpoint.vote(ID, voteDTO);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).vote(any());
    }

}
