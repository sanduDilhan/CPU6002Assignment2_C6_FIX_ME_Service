package com.sentura.bLow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TargetWeightDTO {

    private Long targetWeightId;
    private Long userDetailId;
    private Double weight;
    private String specialNote;
    private Boolean isTargetWeight;
    private Boolean isActive;
    private String actualWeightDate;
    private Date createDate;
}
