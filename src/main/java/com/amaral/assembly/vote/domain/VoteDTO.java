package com.amaral.assembly.vote.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VoteDTO {

    private Integer minuteId;

    private Integer associateId;

    private VotoAnswer answer;

}
