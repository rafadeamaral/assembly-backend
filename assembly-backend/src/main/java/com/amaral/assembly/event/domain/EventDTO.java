package com.amaral.assembly.event.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
public class EventDTO {

    private Integer id;

    @NotBlank(message = "title.not.empty")
    private String title;

    @NotNull(message = "date.not.null")
    private LocalDate date;

}
