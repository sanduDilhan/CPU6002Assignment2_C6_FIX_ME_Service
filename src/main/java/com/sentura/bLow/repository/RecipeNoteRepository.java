package com.sentura.bLow.repository;

import com.sentura.bLow.entity.RecipeNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeNoteRepository extends JpaRepository<RecipeNote, Long> {

    @Query(nativeQuery = true, value = "select * from recipe_note where recipeDetail_recipeDetailId=:recipeId")
    List<RecipeNote> findByRecipeId(@Param("recipeId") Long recipeId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM recipe_note where recipeDetail_recipeDetailId=:recipeId")
    Integer deleteByRecipeId(@Param("recipeId") Long recipeId);
}
