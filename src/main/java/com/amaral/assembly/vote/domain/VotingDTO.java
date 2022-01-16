package com.amaral.assembly.vote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VotingDTO {

    @JsonIgnore
    private Integer id;

    private Long minutes;

}
