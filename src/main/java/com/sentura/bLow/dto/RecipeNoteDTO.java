package com.sentura.bLow.dto;

import com.sentura.bLow.entity.RecipeDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RecipeNoteDTO {

    private Long recipeNoteId;
    private Long recipeDetailId;
    private String description;
    private Date create_date;
}
