package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "recipe_detail")
public class RecipeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeDetailId;

//    @ManyToOne(cascade = CascadeType.ALL)
    private String recipeCategoryId;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserDetail userDetail;

    private String recipeName;
    private Integer preparationTime;
    private Integer cookTime;
    private Integer servingCount;
    private Double carbsPerServe;
    private String imageUrl;
    private String youtubeVideoUrl;
    private Date createDate;
    private Boolean isActive;
    private Boolean isUserCustomRecipe;
    private String additionalTopic;
    private String mainIngredientTopic;

    @Column(length = 60000, columnDefinition = "TEXT")
    private String customRecipeDescription;

    @Override
    public String toString() {
        return "RecipeDetail{" +
                "recipeDetailId=" + recipeDetailId +
                ", recipeCategoryId='" + recipeCategoryId + '\'' +
                ", userDetail=" + userDetail +
                ", recipeName='" + recipeName + '\'' +
                ", preparationTime=" + preparationTime +
                ", cookTime=" + cookTime +
                ", servingCount=" + servingCount +
                ", carbsPerServe=" + carbsPerServe +
                ", imageUrl='" + imageUrl + '\'' +
                ", youtubeVideoUrl='" + youtubeVideoUrl + '\'' +
                ", createDate=" + createDate +
                ", isActive=" + isActive +
                ", isUserCustomRecipe=" + isUserCustomRecipe +
                ", additionalTopic='" + additionalTopic + '\'' +
                ", mainIngredientTopic='" + mainIngredientTopic + '\'' +
                ", customRecipeDescription='" + customRecipeDescription + '\'' +
                '}';
    }
}
