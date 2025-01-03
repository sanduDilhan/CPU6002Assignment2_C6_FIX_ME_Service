package com.sentura.bLow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RecipeInstructionDTO {

    private Long recipeInstructionId;
    private Long recipeDetailId;
    private String description;
    private Date create_date;
}
