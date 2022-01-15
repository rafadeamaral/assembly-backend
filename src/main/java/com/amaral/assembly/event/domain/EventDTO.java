package com.amaral.assembly.event.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class EventDTO {

    private Integer id;

    private String title;

    private LocalDate date;

}
