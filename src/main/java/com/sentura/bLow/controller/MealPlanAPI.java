package com.sentura.bLow.controller;

import com.sentura.bLow.dto.MealPlanDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.service.MealPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/meal-plan")
public class MealPlanAPI {

    @Autowired
    private MealPlanService mealPlanService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> save(@RequestBody MealPlanDTO mealPlanDTO) throws Exception{
        return new ResponseEntity<>(mealPlanService.save(mealPlanDTO), HttpStatus.OK);
    }

    @GetMapping("/get/{mealPlanId}")
    public ResponseEntity<ResponseDto> get(@PathVariable("mealPlanId") long mealPlanId) throws Exception{
        return new ResponseEntity<>(mealPlanService.get(mealPlanId), HttpStatus.OK);
    }

    @GetMapping("/get-all/{userDetailId}")
    public ResponseEntity<ResponseDto> getAll(@PathVariable("userDetailId") long userDetailId) throws Exception{
        return new ResponseEntity<>(mealPlanService.getAll(userDetailId), HttpStatus.OK);
    }

    @PostMapping("/updateMealPlanName")
    public ResponseEntity<ResponseDto> updateMealPlanName(
            @RequestParam(value = "userMealPlanId")Long userMealPlanId,
            @RequestParam(value = "mealPlanName")String mealPlanName
            ) throws Exception{
        return new ResponseEntity<>(mealPlanService.updateMealPlanName(userMealPlanId,mealPlanName), HttpStatus.OK);
    }

    @PostMapping("/deleteMealPlanName")
    public ResponseEntity<ResponseDto> deleteMealPlanName(
            @RequestParam(value = "userMealPlanId")Long userMealPlanId
    ) throws Exception{
        return new ResponseEntity<>(mealPlanService.deleteMealPlan(userMealPlanId), HttpStatus.OK);
    }
}
