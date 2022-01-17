package com.amaral.assembly.vote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class VoteDTO {

    @JsonIgnore
    private Integer agendaId;

    @NotNull(message = "associate.not.null")
    private Integer associateId;

    @NotNull(message = "answer.not.null")
    private VotoAnswer answer;

}
