package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "custom_recipe_ingredient")
public class CustomRecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customRecipeIngredientId;

    @ManyToOne(cascade = CascadeType.ALL)
    private RecipeDetail recipeDetail;

    private String ingredient;
    private String measurementType;
    private Double measurement;
    private Boolean isActive;
    private Date createDate = new Date();

    @Override
    public String toString() {
        return "CustomRecipeIngredient{" +
                "customRecipeIngredientId=" + customRecipeIngredientId +
                ", recipeDetail=" + recipeDetail +
                ", ingredient='" + ingredient + '\'' +
                ", measurementType='" + measurementType + '\'' +
                ", measurement=" + measurement +
                ", isActive=" + isActive +
                ", createDate=" + createDate +
                '}';
    }
}
