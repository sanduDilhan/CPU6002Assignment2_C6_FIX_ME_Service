package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "favourite_additional_ingredient")
public class FavouriteAdditionalIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long additionalIngredientId;

    @ManyToOne(cascade = CascadeType.ALL)
    private FavouriteUserRecipe favouriteUserRecipe;

    @ManyToOne(cascade = CascadeType.ALL)
    private IngredientDetail ingredientDetail;

    private Double measurement;
    private Boolean isActive;
    private Date createDate =  new Date();

    @Override
    public String toString() {
        return "FavouriteAdditionalIngredient{" +
                "additionalIngredientId=" + additionalIngredientId +
                ", favouriteUserRecipe=" + favouriteUserRecipe +
                ", ingredientDetail=" + ingredientDetail +
                ", measurement=" + measurement +
                ", isActive=" + isActive +
                ", createDate=" + createDate +
                '}';
    }
}
