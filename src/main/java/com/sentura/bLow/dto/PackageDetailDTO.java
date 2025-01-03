package com.sentura.bLow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PackageDetailDTO {

    private Long packageId;
    private String packageName;
    private String description;
    private Double packagePrice;
    private Integer validDayCount;
    private Boolean isActive;
    private Date createDate;
}
