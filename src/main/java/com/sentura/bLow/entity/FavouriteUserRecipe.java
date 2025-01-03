package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "favourite_user_recipe")
public class FavouriteUserRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favouriteUserRecipeId;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserDetail userDetail;

    @ManyToOne(cascade = CascadeType.ALL)
    private RecipeDetail recipeDetail;

    @ManyToOne(cascade = CascadeType.ALL)
    private IngredientDetail ingredientDetail;

    private String editFavouriteRecipeName;
    private Double carbsCount;
    private Integer ServingCount;
    private Date createDate;
    private Boolean isActive;

    @Override
    public String toString() {
        return "FavouriteUserRecipe{" +
                "favouriteUserRecipeId=" + favouriteUserRecipeId +
                ", userDetail=" + userDetail +
                ", recipeDetail=" + recipeDetail +
                ", ingredientDetail=" + ingredientDetail +
                ", editFavouriteRecipeName='" + editFavouriteRecipeName + '\'' +
                ", carbsCount=" + carbsCount +
                ", ServingCount=" + ServingCount +
                ", createDate=" + createDate +
                ", isActive=" + isActive +
                '}';
    }
}
