package com.sentura.bLow.repository;

import com.sentura.bLow.entity.MealPlanDetail;
import com.sentura.bLow.entity.UserMealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealPlanDetailRepository extends JpaRepository<MealPlanDetail, Long> {

    List<MealPlanDetail> findByUserMealPlan(UserMealPlan userMealPlan);
}
