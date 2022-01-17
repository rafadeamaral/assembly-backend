package com.amaral.assembly.vote.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class VotingResultDTO {

    private Integer agendaId;

    private Long amountYes;

    private Long amountNo;

    private Long amountTotal;

}
