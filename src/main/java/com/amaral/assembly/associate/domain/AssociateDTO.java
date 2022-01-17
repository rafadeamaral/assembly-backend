package com.amaral.assembly.associate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class AssociateDTO {

    private Integer id;

    @NotBlank(message = "name.not.empty")
    private String name;

    @NotBlank(message = "cpf.not.empty")
    private String cpf;

    private AssociateStatus status;

}
