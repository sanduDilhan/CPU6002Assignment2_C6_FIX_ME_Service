package com.sentura.bLow.dto;

import lombok.Data;

import java.util.List;

@Data
public class MealPlanDTO {

    private UserMealPlanDTO userMealPlanDTO;
    private List<MealPlanDetailDTO> mealPlanDetailDTOList;
}
