package com.amaral.assembly.agenda.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AgendaDTO {

    private Integer id;

    private String title;

    private String description;

    private AgendaStatus status;

    private LocalDateTime finalVoting;

    private Integer eventId;

}
