package com.sentura.bLow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class IngredientDetailDTO {

    private Long ingredientDetailId;
    private String ingredient;
    private String brand;
    private String subTitle;
    private String externalRecipe;
    private Integer tspOrQty;
    private String measurementType;
    private Double measurement;
    private Double carbs;
    private Date createDate;
    private Boolean isHide;
}
