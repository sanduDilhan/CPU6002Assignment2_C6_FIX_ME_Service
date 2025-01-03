package com.sentura.bLow.controller;


import com.sentura.bLow.dto.FavouriteUserRecipeDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.service.FavouriteUserRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/favouriteRecipe")
public class FavouriteUserRecipeController {

    @Autowired
    private FavouriteUserRecipeService favouriteUserRecipeService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> saveFavouriteRecipe(@Valid @RequestBody FavouriteUserRecipeDTO favouriteUserRecipeDTO) throws Exception{
        return new ResponseEntity<>(favouriteUserRecipeService.createFavouriteRecipeAndIngredient(favouriteUserRecipeDTO), HttpStatus.OK);
    }

    @PostMapping(value = "/saveCustomRecipeFavourite", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto> saveCustomRecipe(
            @RequestParam(value = "recipeDetail") String recipeDetail,
            @RequestParam(value = "recipeImage", required = false) MultipartFile recipeImage) throws Exception{
        return new ResponseEntity<>(favouriteUserRecipeService.createCustomRecipeFavourite(recipeDetail,recipeImage), HttpStatus.OK);
    }

    @GetMapping("/removeFavourite/{favouriteRecipeId}")
    public ResponseEntity<ResponseDto> deleteFavouriteRecipe(@PathVariable("favouriteRecipeId")Long favouriteRecipeId) throws Exception{
        return new ResponseEntity<>(favouriteUserRecipeService.deleteFavouriteRecipeAndIngredient(favouriteRecipeId), HttpStatus.OK);
    }

    @GetMapping("/getAllFavouritesByUserId/{userId}")
    public ResponseEntity<ResponseDto> getAllFavouriteRecipeAndIngredientByUserId(@PathVariable("userId")Long userId) throws Exception{
        return new ResponseEntity<>(favouriteUserRecipeService.getAllFavouriteRecipeAndIngredientByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/getFavouriteById/{favouriteId}")
    public ResponseEntity<ResponseDto> getFavouriteById(@PathVariable("favouriteId")Long favouriteId) throws Exception{
        return new ResponseEntity<>(favouriteUserRecipeService.getFavouriteRecipeAndIngredientById(favouriteId), HttpStatus.OK);
    }

    @PostMapping("/updateFavouriteRecipe")
    public ResponseEntity<ResponseDto> updateFavouriteRecipe(@Valid @RequestBody FavouriteUserRecipeDTO favouriteUserRecipeDTO) throws Exception{
        return new ResponseEntity<>(favouriteUserRecipeService.updateFavouriteRecipe(favouriteUserRecipeDTO), HttpStatus.OK);
    }

    @PostMapping("/updateFavouriteRecipeServeCount")
    public ResponseEntity<ResponseDto> updateFavouriteRecipeServeCount(@Valid @RequestBody FavouriteUserRecipeDTO favouriteUserRecipeDTO) throws Exception{
        return new ResponseEntity<>(favouriteUserRecipeService.updateFavouriteRecipeServeCount(favouriteUserRecipeDTO), HttpStatus.OK);
    }
}
