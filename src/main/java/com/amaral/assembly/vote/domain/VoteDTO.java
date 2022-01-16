package com.amaral.assembly.vote.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VoteDTO {

    private Integer agendaId;

    private Integer associateId;

    private VotoAnswer answer;

}
