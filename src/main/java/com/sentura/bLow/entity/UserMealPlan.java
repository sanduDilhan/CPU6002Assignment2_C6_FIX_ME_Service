package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "user_meal_plan")
public class UserMealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userMealPlanId;

    @ManyToOne
    @JoinColumn
    private UserDetail userDetail;

    private String mealPlanName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDateTime = new Date();

    private Boolean isActive;

    @Override
    public String toString() {
        return "UserMealPlan{" +
                "userMealPlanId=" + userMealPlanId +
                ", userDetail=" + userDetail +
                ", mealPlanName='" + mealPlanName + '\'' +
                ", createDateTime=" + createDateTime +
                ", isActive=" + isActive +
                '}';
    }
}
