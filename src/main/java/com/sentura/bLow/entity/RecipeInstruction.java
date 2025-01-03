package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "recipe_instruction")
public class RecipeInstruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeInstructionId;

    @ManyToOne(cascade = CascadeType.ALL)
    private RecipeDetail recipeDetail;

    @Column(length = 60000, columnDefinition = "TEXT")
    private String description;
    private Date create_date;

    @Override
    public String toString() {
        return "RecipeInstruction{" +
                "recipeInstructionId=" + recipeInstructionId +
                ", recipeDetail=" + recipeDetail +
                ", description='" + description + '\'' +
                ", create_date=" + create_date +
                '}';
    }
}
