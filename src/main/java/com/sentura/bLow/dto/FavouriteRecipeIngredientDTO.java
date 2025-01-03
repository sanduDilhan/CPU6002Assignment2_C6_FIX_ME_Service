package com.sentura.bLow.dto;


import com.sentura.bLow.entity.IngredientDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FavouriteRecipeIngredientDTO {

    private Long favouriteRecipeIngredientId;
    private Long favouriteUserRecipeId;
    private Long ingredientDetailId;
    private Double measurement;
    private Date createDate;
    private Boolean isActive;

    private IngredientDetail ingredientDetail;
}
