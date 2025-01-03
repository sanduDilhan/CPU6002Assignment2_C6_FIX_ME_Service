package com.sentura.bLow.repository;

import com.sentura.bLow.entity.MealPlanDetail;
import com.sentura.bLow.entity.MealPlanRecipeIngriedient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealPlanRecipeIngriedientRepository extends JpaRepository<MealPlanRecipeIngriedient, Long> {

    List<MealPlanRecipeIngriedient> findByMealPlanDetail(MealPlanDetail mealPlanDetail);
}
