package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "recipe_ingredient")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeIngredientId;

    @ManyToOne(cascade = CascadeType.ALL)
    private RecipeDetail recipeDetail;

    @ManyToOne(cascade = CascadeType.ALL)
    private IngredientDetail ingredientDetail;

    private Double measurement;
    private Boolean isActive;
    private Date createDate;

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "recipeIngredientId=" + recipeIngredientId +
                ", recipeDetail=" + recipeDetail +
                ", ingredientDetail=" + ingredientDetail +
                ", measurement=" + measurement +
                ", isActive=" + isActive +
                ", createDate=" + createDate +
                '}';
    }
}
