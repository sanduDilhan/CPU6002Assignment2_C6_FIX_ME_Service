package com.sentura.bLow.repository;

import com.sentura.bLow.entity.AdditionalIngredient;
import com.sentura.bLow.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdditionalIngredientRepository extends JpaRepository<AdditionalIngredient,Long> {
    @Query(nativeQuery = true, value = "select * from additional_ingredient where recipeDetail_recipeDetailId=:recipeId AND isActive=true")
    List<AdditionalIngredient> findByRecipeId(@Param("recipeId") Long recipeId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM additional_ingredient where recipeDetail_recipeDetailId=:recipeId")
    Integer deleteByRecipeId(@Param("recipeId") Long recipeId);
}
