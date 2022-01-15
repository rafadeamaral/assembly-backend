package com.amaral.assembly.minute.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class MinuteDTO {

    private Integer id;

    private String title;

    private String description;

    private MinuteStatus status;

    private LocalDateTime finalVoting;

    private Integer eventId;

}
