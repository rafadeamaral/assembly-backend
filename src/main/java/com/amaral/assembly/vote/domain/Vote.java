package com.amaral.assembly.vote.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Setter
@Getter
@Entity
public class Vote {

    @EmbeddedId
    private VoteId id;

    @Enumerated(EnumType.ORDINAL)
    private VotoAnswer answer;

}
