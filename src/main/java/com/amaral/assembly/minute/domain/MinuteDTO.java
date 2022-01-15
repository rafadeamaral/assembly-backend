package com.amaral.assembly.minute.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MinuteDTO {

    private Integer id;

    private String title;

    private String description;

    private MinuteStatus status;

    private Integer eventId;

}
