package com.amaral.assembly.agenda.domain;

import com.amaral.assembly.event.domain.Event;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String title;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    private AgendaStatus status;

    private LocalDateTime finalVoting;

    @ManyToOne
    private Event event;

}
