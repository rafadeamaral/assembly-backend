package com.amaral.assemblyproducer.message.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Message {

    private Integer agendaId;

    private Long amountYes;

    private Long amountNo;

    private Long amountTotal;

}
