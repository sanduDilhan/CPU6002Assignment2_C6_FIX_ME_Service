package com.sentura.bLow.repository;

import com.sentura.bLow.entity.RecipeInstruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeInstructionRepository extends JpaRepository<RecipeInstruction, Long> {

    @Query(nativeQuery = true, value = "select * from recipe_instruction where recipeDetail_recipeDetailId=:recipeId")
    List<RecipeInstruction> findByRecipeId(@Param("recipeId") Long recipeId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM recipe_instruction where recipeDetail_recipeDetailId=:recipeId")
    Integer deleteByRecipeId(@Param("recipeId") Long recipeId);
}
