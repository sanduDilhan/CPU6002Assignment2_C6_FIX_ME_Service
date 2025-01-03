package com.sentura.bLow.repository;

import com.sentura.bLow.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    @Query(nativeQuery = true, value = "select * from recipe_ingredient where recipeDetail_recipeDetailId=:recipeId AND isActive=true")
    List<RecipeIngredient> findByRecipeId(@Param("recipeId") Long recipeId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM recipe_ingredient where recipeDetail_recipeDetailId=:recipeId")
    Integer deleteByRecipeId(@Param("recipeId") Long recipeId);
}
