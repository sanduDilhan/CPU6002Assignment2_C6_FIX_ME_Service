package com.sentura.bLow.dto;

import com.sentura.bLow.entity.RecipeCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RecipeDetailDTO {

    private Long recipeDetailId;
    private String recipeCategoryId;
    private Long userDetailId;
    private String recipeName;
    private Integer preparationTime;
    private Integer cookTime;
    private Integer servingCount;
    private Double carbsPerServe;
    private String imageUrl;
    private String youtubeVideoUrl;
    private Date createDate;
    private Boolean isActive;
    private Boolean isUserCustomRecipe;
    private String additionalTopic;
    private String mainIngredientTopic;
    private String customRecipeDescription;

//    private RecipeCategory recipeCategory;
    private List<RecipeCategory> recipeCategoryList;

    private List<RecipeIngredientDTO> recipeIngredientDTOList;
    private List<RecipeInstructionDTO> recipeInstructionDTOList;
    private List<RecipeNoteDTO> recipeNoteDTOList;
    private List<AdditionalIngredientDTO> additionalIngredientDTOList;

    private List<CustomRecipeIngredientDTO> customRecipeIngredientDTOList;
}
