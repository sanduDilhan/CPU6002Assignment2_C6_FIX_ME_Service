package com.sentura.bLow.repository;

import com.sentura.bLow.entity.UserDetail;
import com.sentura.bLow.entity.UserMealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMealPlanRepository extends JpaRepository<UserMealPlan, Long> {

    List<UserMealPlan> findByUserDetailAndIsActiveTrue(UserDetail userDetail);
}
