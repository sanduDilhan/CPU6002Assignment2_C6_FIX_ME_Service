package com.sentura.bLow.dto;

import com.sentura.bLow.entity.UserMealPlan;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MealPlanDetailDTO {

    private Long mealPlanDetailId;
    private UserMealPlanDTO userMealPlanDTO;
    private String dayName;
    private Date createDate;
    private List<MealPlanRecipeIngriedientDTO> mealPlanRecipeIngriedientDTOList;
}
