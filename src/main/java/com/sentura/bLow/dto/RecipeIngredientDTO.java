package com.sentura.bLow.dto;

import com.sentura.bLow.entity.IngredientDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RecipeIngredientDTO {

    private Long recipeIngredientId;
    private Long recipeDetailId;
    private Long ingredientDetailId;
    private Double measurement;
    private Boolean isActive;
    private Date createDate;

    private IngredientDetail ingredientDetail;
}
