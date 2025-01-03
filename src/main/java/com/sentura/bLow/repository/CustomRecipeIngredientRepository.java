package com.sentura.bLow.repository;

import com.sentura.bLow.entity.CustomRecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomRecipeIngredientRepository extends JpaRepository<CustomRecipeIngredient, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM custom_recipe_ingredient where recipeDetail_recipeDetailId=:recipeId")
    Integer deleteByCustomRecipeId(@Param("recipeId") Long recipeId);

    @Query(nativeQuery = true, value = "select * from custom_recipe_ingredient where recipeDetail_recipeDetailId=:recipeId")
    List<CustomRecipeIngredient> getAllByCustomRecipeId(@Param("recipeId") Long recipeId);
}
