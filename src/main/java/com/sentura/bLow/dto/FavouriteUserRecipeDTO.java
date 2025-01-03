package com.sentura.bLow.dto;

import com.sentura.bLow.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FavouriteUserRecipeDTO {

    private Long favouriteUserRecipeId;
    private Long userDetailId;
    private Long recipeDetailId;
    private Long ingredientDetailId;
    private Double carbsCount;
    private Integer servingCount;
    private Date createDate;
    private Boolean isActive;
    private String editFavouriteRecipeName;

    private RecipeDetail recipeDetail;
    private IngredientDetail ingredientDetail;

    private List<FavouriteRecipeIngredientDTO> favouriteRecipeIngredientDTOList;
    private List<RecipeInstructionDTO> recipeInstructionDTOList;
    private List<RecipeNoteDTO> recipeNoteDTOList;
    private List<FavouriteAdditionalIngredientDTO> favouriteAdditionalIngredientDTOList;
    private List<CustomRecipeIngredientDTO> customRecipeIngredientDTOList;
}
