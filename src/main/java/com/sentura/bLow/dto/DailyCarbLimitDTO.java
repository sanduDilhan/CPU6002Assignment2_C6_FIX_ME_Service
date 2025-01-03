package com.sentura.bLow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DailyCarbLimitDTO {

    private Long dailyCarbLimitId;
    private Long userDetailId;
    private Double carbLimit;
    private Boolean isActive;
    private Date createDate;
}
