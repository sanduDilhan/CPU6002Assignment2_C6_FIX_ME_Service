package com.sentura.bLow.service;

import com.sentura.bLow.dto.RecipeCategoryDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.entity.RecipeCategory;
import com.sentura.bLow.repository.RecipeCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RecipeCategoryService {

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto createCategory(RecipeCategoryDTO recipeCategoryDTO) throws Exception {
        RecipeCategory recipeCategory = new RecipeCategory();
        recipeCategory.setRecipeCategoryId(0L);
        recipeCategory.setCategoryName(recipeCategoryDTO.getCategoryName());
        recipeCategory.setCreateDate(new Date());

        recipeCategoryRepository.save(recipeCategory);
        return new ResponseDto("success", "200", null);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto updateCategory(RecipeCategoryDTO recipeCategoryDTO) throws Exception {
        RecipeCategory recipeCategory = new RecipeCategory();
        recipeCategory.setRecipeCategoryId(recipeCategoryDTO.getRecipeCategoryId());
        recipeCategory.setCategoryName(recipeCategoryDTO.getCategoryName());
        recipeCategory.setCreateDate(new Date());

        recipeCategoryRepository.save(recipeCategory);
        return new ResponseDto("success", "200", null);
    }

    public ResponseDto getAllCategory() throws Exception {
        List<RecipeCategory> recipeCategoryList = recipeCategoryRepository.findAll();
        return new ResponseDto("success", "200", recipeCategoryList);
    }

    public ResponseDto findByCategory(Long categoryId) throws Exception {
        RecipeCategory recipeCategory = recipeCategoryRepository.findById(categoryId).get();
        return new ResponseDto("success", "200", recipeCategory);
    }

}
