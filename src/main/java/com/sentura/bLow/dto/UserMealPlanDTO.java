package com.sentura.bLow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserMealPlanDTO {

    private Long userMealPlanId;
    private Long userDetailId;
    private String mealPlanName;
    private Date createDate;
    private Boolean isActive;
}
