package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "favourite_recipi_ingredient")
public class FavouriteRecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favouriteRecipeIngredientId;

    @ManyToOne(cascade = CascadeType.ALL)
    private FavouriteUserRecipe favouriteUserRecipe;

    @ManyToOne(cascade = CascadeType.ALL)
    private IngredientDetail ingredientDetail;

    private Double measurement;
    private Date createDate;
    private Boolean isActive;

    @Override
    public String toString() {
        return "FavouriteRecipeIngredient{" +
                "favouriteRecipeIngredientId=" + favouriteRecipeIngredientId +
                ", favouriteUserRecipe=" + favouriteUserRecipe +
                ", ingredientDetail=" + ingredientDetail +
                ", measurement=" + measurement +
                ", createDate=" + createDate +
                ", isActive=" + isActive +
                '}';
    }
}
