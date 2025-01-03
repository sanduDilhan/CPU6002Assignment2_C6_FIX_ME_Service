package com.sentura.bLow.repository;

import com.sentura.bLow.entity.RecipeDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeDetailRepository extends JpaRepository<RecipeDetail, Long> {

//    @Query(nativeQuery = true, value = "select * from recipe_ingredient where recipeDetail_recipeDetailId=:recipeId AND isActive=true")
    List<RecipeDetail> findAllByIsUserCustomRecipeFalseAndIsActiveTrue();

    List<RecipeDetail> findAllByIsUserCustomRecipeFalseAndIsActiveTrueOrderByRecipeNameAsc();

    Page<RecipeDetail> findAllByIsUserCustomRecipeFalseAndIsActiveTrue(Pageable paging);

    @Query(nativeQuery = true, value = "select * from recipe_detail where userDetail_userId=:userId and isUserCustomRecipe=true")
    List<RecipeDetail> getAllCustomRecipeByUserId(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = "select count(*) from recipe_detail where isUserCustomRecipe=false AND isActive=true")
    Integer activeRecipeCount();

    @Query(nativeQuery = true, value = "Select * from recipe_detail where FIND_IN_SET(:categoryId, recipeCategoryId)>0 and isUserCustomRecipe=false and isActive=true")
    List<RecipeDetail> getAllRecipeBycategoryId(@Param("categoryId") Long categoryId);

    @Query(nativeQuery = true, value = "Select * from recipe_detail where FIND_IN_SET(:categoryId, recipeCategoryId)>0 and isUserCustomRecipe=false and isActive=true")
    Page<RecipeDetail> getAllRecipeBycategoryIdWithPagination(@Param("categoryId") Long categoryId, Pageable paging);

    @Query(nativeQuery = true, value = "Select * from recipe_detail where recipeName LIKE %:searchWord% and isUserCustomRecipe=false and isActive=true")
    Page<RecipeDetail> searchRecipe(@Param("searchWord") String searchWord, Pageable paging);
}
