package com.sentura.bLow.repository;

import com.sentura.bLow.entity.FavouriteRecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavouriteRecipeIngredientRepository extends JpaRepository<FavouriteRecipeIngredient, Long> {

    @Query(nativeQuery = true, value = "select * from favourite_recipi_ingredient where favouriteUserRecipe_favouriteUserRecipeId=:favouriteRecipeId")
    List<FavouriteRecipeIngredient> getAllFavouriteByUserId(@Param("favouriteRecipeId") Long favouriteRecipeId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM favourite_recipi_ingredient where favouriteUserRecipe_favouriteUserRecipeId=:recipeId")
    Integer deleteByRecipeId(@Param("recipeId") Long recipeId);
}
