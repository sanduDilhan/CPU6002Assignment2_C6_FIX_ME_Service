package com.sentura.bLow.controller;

import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.service.RecipeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/recipe")
public class RecipeDetailAPI {

    @Autowired
    private RecipeDetailService recipeDetailService;

    @PostMapping(value = "/save", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto> saveRecipe(
            @RequestParam(value = "recipeDetail") String recipeDetail,
            @RequestParam(value = "recipeImage", required = false) MultipartFile recipeImage) throws Exception{
        return new ResponseEntity<>(recipeDetailService.createRecipe(recipeDetail,recipeImage), HttpStatus.OK);
    }

    @PostMapping(value = "/update", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto> updateRecipe(
            @RequestParam(value = "recipeDetail") String recipeDetail,
            @RequestParam(value = "recipeImage", required = false) MultipartFile recipeImage) throws Exception{
        return new ResponseEntity<>(recipeDetailService.updateRecipe(recipeDetail,recipeImage), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDto> getAllRecipes() throws Exception{
        return new ResponseEntity<>(recipeDetailService.getAllRecipeDetails(), HttpStatus.OK);
    }

    @GetMapping("/findById/{recipeId}")
    public ResponseEntity<ResponseDto> getRecipeFindById(@PathVariable("recipeId") Long recipeId) throws Exception{
        return new ResponseEntity<>(recipeDetailService.getRecipeDetailsFindById(recipeId), HttpStatus.OK);
    }

    @PostMapping(value = "/saveCustomRecipe", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto> saveCustomRecipe(
            @RequestParam(value = "recipeDetail") String recipeDetail,
            @RequestParam(value = "recipeImage", required = false) MultipartFile recipeImage,
            @RequestParam(value = "mealPlan") String mealPlan,
            @RequestParam(value = "mealPlanDayName") String mealPlanDayName,
            @RequestParam(value = "mealPlanMealSession") String mealPlanMealSession) throws Exception{
        return new ResponseEntity<>(recipeDetailService.createCustomRecipe(recipeDetail,recipeImage,mealPlan,mealPlanDayName,mealPlanMealSession), HttpStatus.OK);
    }

    @PostMapping(value = "/updateCustomRecipe", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto> UpdateCustomRecipe(
            @RequestParam(value = "recipeDetail") String recipeDetail,
            @RequestParam(value = "recipeImage", required = false) MultipartFile recipeImage) throws Exception{
        return new ResponseEntity<>(recipeDetailService.updateCustomRecipe(recipeDetail,recipeImage), HttpStatus.OK);
    }

    @GetMapping("/getCustomRecipeById/{recipeId}")
    public ResponseEntity<ResponseDto> getCustomRecipeById(@PathVariable("recipeId") Long recipeId) throws Exception{
        return new ResponseEntity<>(recipeDetailService.getCustomRecipeById(recipeId), HttpStatus.OK);
    }

    @GetMapping("/getAllCustomRecipe/{userId}")
    public ResponseEntity<ResponseDto> getAllCustomRecipes(@PathVariable("userId") Long userId) throws Exception{
        return new ResponseEntity<>(recipeDetailService.getAllCustomRecipeByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/findByCategoryId/{categoryId}")
    public ResponseEntity<ResponseDto> getAllRecipesByCategoryId(@PathVariable("categoryId") Long categoryId) throws Exception{
        return new ResponseEntity<>(recipeDetailService.getAllRecipebyCategoryId(categoryId), HttpStatus.OK);
    }

    @GetMapping("/deleteRecipe/{recipeId}")
    public ResponseEntity<ResponseDto> deleteRecipeDetail(@PathVariable("recipeId") Long recipeId) throws Exception{
        return new ResponseEntity<>(recipeDetailService.deleteRecipesById(recipeId), HttpStatus.OK);
    }

    @GetMapping("/getAllWithPagination/{pageNo}/{pageCount}/{categoryId}/{searchRec}")
    public ResponseEntity<ResponseDto> getAllRecipesWithPagination(@PathVariable("pageNo") int pageNo, @PathVariable("pageCount") int pageCount, @PathVariable("categoryId") Long categoryId, @PathVariable("searchRec") String searchRec) throws Exception{
        return new ResponseEntity<>(recipeDetailService.getAllRecipeDetailsWithPagination(pageNo, pageCount, categoryId, searchRec), HttpStatus.OK);
    }

    @GetMapping("/findByCategoryIdWithPagination/{categoryId}/{pageNo}/{pageCount}")
    public ResponseEntity<ResponseDto> getAllRecipesByCategoryIdWithPagination(@PathVariable("categoryId") Long categoryId, @PathVariable("pageNo") int pageNo, @PathVariable("pageCount") int pageCount) throws Exception{
        return new ResponseEntity<>(recipeDetailService.getAllRecipebyCategoryIdWithPagination(categoryId,pageNo,pageCount), HttpStatus.OK);
    }

    @GetMapping("/getAllRecipeDetailsDropDown")
    public ResponseEntity<ResponseDto> getAllRecipeDetailsDropDown() throws Exception{
        return new ResponseEntity<>(recipeDetailService.getAllRecipeDetailsDropDown(), HttpStatus.OK);
    }

    @GetMapping("/writeCSV")
    public ResponseEntity<ResponseDto> recipeDetailWriteCSV() throws Exception{
        return new ResponseEntity<>(recipeDetailService.recipeDetailWriteCSV(), HttpStatus.OK);
    }
}
