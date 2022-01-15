package com.amaral.assembly.associate.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssociateDTO {

    private Integer id;

    private String name;

    private String cpf;

    private AssociateStatus status;

}
