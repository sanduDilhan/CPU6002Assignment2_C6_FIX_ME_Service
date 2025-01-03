package com.sentura.bLow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RecipeCategoryDTO {

    private Long recipeCategoryId;
    private String categoryName;
    private Date createDate;
}
