package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "recipe_category")
public class RecipeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeCategoryId;

    private String categoryName;
    private Date createDate;

    @Override
    public String toString() {
        return "RecipeCategory{" +
                "recipeCategoryId=" + recipeCategoryId +
                ", categoryName='" + categoryName + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
