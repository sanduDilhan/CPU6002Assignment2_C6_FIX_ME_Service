package com.sentura.bLow.controller;

import com.sentura.bLow.dto.RecipeCategoryDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.service.RecipeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/recipeCategory")
public class RecipeCategoryAPI {

    @Autowired
    private RecipeCategoryService recipeCategoryService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> saveRecipeCategory(@Valid @RequestBody RecipeCategoryDTO recipeCategoryDTO) throws Exception{
        return new ResponseEntity<>(recipeCategoryService.createCategory(recipeCategoryDTO), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDto> updateRecipeCategory(@Valid @RequestBody RecipeCategoryDTO recipeCategoryDTO) throws Exception{
        return new ResponseEntity<>(recipeCategoryService.updateCategory(recipeCategoryDTO), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDto> getAllRecipeCategory() throws Exception{
        return new ResponseEntity<>(recipeCategoryService.getAllCategory(), HttpStatus.OK);
    }

    @GetMapping("/findById/{categoryId}")
    public ResponseEntity<ResponseDto> findByRecipeCategoryId(@PathVariable("categoryId")Long categoryId) throws Exception{
        return new ResponseEntity<>(recipeCategoryService.findByCategory(categoryId), HttpStatus.OK);
    }
}
