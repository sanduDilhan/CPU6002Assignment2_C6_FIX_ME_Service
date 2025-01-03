package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "additional_ingredient")
public class AdditionalIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long additionalIngredientId;

    @ManyToOne(cascade = CascadeType.ALL)
    private RecipeDetail recipeDetail;

    @ManyToOne(cascade = CascadeType.ALL)
    private IngredientDetail ingredientDetail;

    private Double measurement;
    private Boolean isActive;
    private Date createDate =  new Date();

    @Override
    public String toString() {
        return "AdditionalIngredient{" +
                "additionalIngredientId=" + additionalIngredientId +
                ", recipeDetail=" + recipeDetail +
                ", ingredientDetail=" + ingredientDetail +
                ", measurement=" + measurement +
                ", isActive=" + isActive +
                ", createDate=" + createDate +
                '}';
    }
}
