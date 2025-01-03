package com.sentura.bLow.dto;

import com.sentura.bLow.entity.RecipeDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CustomRecipeIngredientDTO {

    private Long customRecipeIngredientId;
    private Long recipeDetailId;
    private String ingredient;
    private String measurementType;
    private Double measurement;
    private Boolean isActive;
    private Date createDate = new Date();

    private RecipeDetail recipeDetail;
}
