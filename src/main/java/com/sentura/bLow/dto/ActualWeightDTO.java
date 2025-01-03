package com.sentura.bLow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ActualWeightDTO {

    private Long actualWeightId;
    private Long userDetailId;
    private Double weight;
    private String specialNote;
    private Date weightExpectedDate;
    private Date createDate;
}
