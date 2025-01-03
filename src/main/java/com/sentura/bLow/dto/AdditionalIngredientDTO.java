package com.sentura.bLow.dto;

import com.sentura.bLow.entity.IngredientDetail;
import com.sentura.bLow.entity.RecipeDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdditionalIngredientDTO {

    private Long additionalIngredientId;
    private Long recipeDetailId;
    private Long ingredientDetailId;
    private Double measurement;
    private Boolean isActive;
    private Date createDate;

    private RecipeDetail recipeDetail;
    private IngredientDetail ingredientDetail;
}
