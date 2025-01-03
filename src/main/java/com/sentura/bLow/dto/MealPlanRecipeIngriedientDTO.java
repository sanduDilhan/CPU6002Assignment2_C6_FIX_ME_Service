package com.sentura.bLow.dto;

import com.sentura.bLow.entity.FavouriteUserRecipe;
import com.sentura.bLow.entity.IngredientDetail;
import com.sentura.bLow.entity.MealPlanDetail;
import com.sentura.bLow.entity.RecipeDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MealPlanRecipeIngriedientDTO {

    private Long mealPlanRecipeIngriedientId;
    private MealPlanDetailDTO mealPlanDetailDTO;
    private RecipeDetailDTO recipeDetailDTO;
    private IngredientDetailDTO ingredientDetailDTO;
    private FavouriteUserRecipeDTO favouriteUserRecipeDTO;
    private String mealSession;
    private Integer servingCount;
    private Date createDate;

    private List<CustomRecipeIngredientDTO> customRecipeIngredientDTOList;
}
