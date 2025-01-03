package com.sentura.bLow.dto;

import com.sentura.bLow.entity.PackageDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserPackageDTO {

    private Long userPackageId;
    private Long userDetailId;
    private PackageDetail packageDetail;
    private Double packagePrice;
    private Integer validDayCount;
    private Date startDate;
    private Date initiateDate;
    private Long cycleDate;
    private Date expireDate;
    private Boolean isActive;
    private Boolean isTrialActive;

    private Boolean isExpired;
    private Date createDate;
}
