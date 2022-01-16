package com.amaral.assembly.agenda.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
public class AgendaDTO {

    private Integer id;

    @NotBlank(message = "title.not.empty")
    private String title;

    @NotBlank(message = "description.not.empty")
    private String description;

    private AgendaStatus status;

    private LocalDateTime finalVoting;

    @NotNull(message = "event.not.null")
    private Integer eventId;

}
