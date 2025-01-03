package com.sentura.bLow.repository;

import com.sentura.bLow.entity.FavouriteUserRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavouriteUserRecipeRepository extends JpaRepository<FavouriteUserRecipe, Long> {

    @Query(nativeQuery = true, value = "select * from favourite_user_recipe where userDetail_userId=:userId and isActive=true")
    List<FavouriteUserRecipe> getAllFavouriteByUserId(@Param("userId") Long userId);
}
