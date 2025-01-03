package com.sentura.bLow.repository;

import com.sentura.bLow.entity.FavouriteAdditionalIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavouriteAdditionalIngredientRepository extends JpaRepository<FavouriteAdditionalIngredient, Long> {

    @Query(nativeQuery = true, value = "select * from favourite_additional_ingredient where favouriteUserRecipe_favouriteUserRecipeId=:favouriteRecipeId")
    List<FavouriteAdditionalIngredient> getAllFavouriteByRecipeId(@Param("favouriteRecipeId") Long favouriteRecipeId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM favourite_additional_ingredient where favouriteUserRecipe_favouriteUserRecipeId=:recipeId")
    Integer deleteByRecipeId(@Param("recipeId") Long recipeId);
}
