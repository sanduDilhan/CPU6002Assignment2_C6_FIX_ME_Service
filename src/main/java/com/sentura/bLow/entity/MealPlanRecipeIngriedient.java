package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "meal_plan_recipe_ingriedient")
public class MealPlanRecipeIngriedient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealPlanRecipeIngriedientId;

    @ManyToOne
    @JoinColumn
    private MealPlanDetail mealPlanDetail;

    @ManyToOne
    @JoinColumn
    private RecipeDetail recipeDetail;

    @ManyToOne
    @JoinColumn
    private IngredientDetail ingredientDetail;

    @ManyToOne
    @JoinColumn
    private FavouriteUserRecipe favouriteUserRecipe;

    private String mealSession;

    private Integer ServingCount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDateTime = new Date();

    @Override
    public String toString() {
        return "MealPlanRecipeIngriedient{" +
                "mealPlanRecipeIngriedientId=" + mealPlanRecipeIngriedientId +
                ", mealPlanDetail=" + mealPlanDetail +
                ", recipeDetail=" + recipeDetail +
                ", ingredientDetail=" + ingredientDetail +
                ", favouriteUserRecipe=" + favouriteUserRecipe +
                ", mealSession='" + mealSession + '\'' +
                ", createDate=" + createDateTime +
                '}';
    }
}
