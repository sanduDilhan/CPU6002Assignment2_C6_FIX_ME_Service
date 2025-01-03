package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "ingredient_detail")
public class IngredientDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public String toString() {
        return "IngredientDetail{" +
                "ingredientDetailId=" + ingredientDetailId +
                ", ingredient='" + ingredient + '\'' +
                ", brand='" + brand + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", externalRecipe='" + externalRecipe + '\'' +
                ", tspOrQty=" + tspOrQty +
                ", measurementType='" + measurementType + '\'' +
                ", measurement=" + measurement +
                ", carbs=" + carbs +
                ", createDate=" + createDate +
                ", isHide=" + isHide +
                '}';
    }
}
