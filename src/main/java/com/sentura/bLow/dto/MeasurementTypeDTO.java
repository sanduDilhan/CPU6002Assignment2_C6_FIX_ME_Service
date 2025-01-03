package com.sentura.bLow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MeasurementTypeDTO {

    private Long measurementTypeId;
    private String measurementName;
    private Double measurement;
    private Boolean isActive;
    private Date createDate;
}
