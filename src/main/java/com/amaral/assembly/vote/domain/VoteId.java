package com.amaral.assembly.vote.domain;

import com.amaral.assembly.associate.domain.Associate;
import com.amaral.assembly.minute.domain.Minute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Setter
@Getter
@Embeddable
public class VoteId implements Serializable {

    @ManyToOne
    private Minute minute;

    @ManyToOne
    private Associate associate;

}
