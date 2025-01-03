package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "meal_plan_detail")
public class MealPlanDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealPlanDetailId;

    @ManyToOne
    @JoinColumn
    private UserMealPlan userMealPlan;

    private String dayName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDateTime = new Date();

    @Override
    public String toString() {
        return "MealPlanDetail{" +
                "mealPlanDetailId=" + mealPlanDetailId +
                ", userMealPlan=" + userMealPlan +
                ", dayName='" + dayName + '\'' +
                ", createDate=" + createDateTime +
                '}';
    }
}
