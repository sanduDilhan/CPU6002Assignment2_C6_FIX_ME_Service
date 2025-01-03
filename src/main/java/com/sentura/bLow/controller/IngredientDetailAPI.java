package com.sentura.bLow.controller;

import com.sentura.bLow.dto.IngredientDetailDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.service.IngredientDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/ingredient")
public class IngredientDetailAPI {

    @Autowired
    private IngredientDetailService ingredientDetailService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> saveIngredient(@Valid @RequestBody IngredientDetailDTO ingredientDetailDTO) throws Exception{
        return new ResponseEntity<>(ingredientDetailService.createIngredient(ingredientDetailDTO), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDto> updateIngredient(@Valid @RequestBody IngredientDetailDTO ingredientDetailDTO) throws Exception{
        return new ResponseEntity<>(ingredientDetailService.updateIngredient(ingredientDetailDTO), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDto> getAllIngredient() throws Exception{
        return new ResponseEntity<>(ingredientDetailService.getAllIngredient(), HttpStatus.OK);
    }

    @GetMapping("/findById/{ingredientId}")
    public ResponseEntity<ResponseDto> findByIngredient(@PathVariable("ingredientId")Long ingredientId) throws Exception{
        return new ResponseEntity<>(ingredientDetailService.findByIngredientId(ingredientId), HttpStatus.OK);
    }

    @GetMapping("/getAllActiveIngredient")
    public ResponseEntity<ResponseDto> getAllActiveIngredient() throws Exception{
        return new ResponseEntity<>(ingredientDetailService.getAllActiveIngredient(), HttpStatus.OK);
    }

    @GetMapping("/updateIngredientVisibility/{ingredientId}")
    public ResponseEntity<ResponseDto> updateIngredientVisibility(@PathVariable("ingredientId")Long ingredientId) throws Exception{
        return new ResponseEntity<>(ingredientDetailService.hideAndShowIngredient(ingredientId), HttpStatus.OK);
    }

    @GetMapping("/getAllActiveIngredientPublicSite")
    public ResponseEntity<ResponseDto> getAllActiveIngredientPublicSite() throws Exception{
        return new ResponseEntity<>(ingredientDetailService.getAllActiveIngredientByPublicSite(), HttpStatus.OK);
    }

    @GetMapping("/getAllActiveIngredientAdminSite")
    public ResponseEntity<ResponseDto> getAllActiveIngredientAdminSite() throws Exception{
        return new ResponseEntity<>(ingredientDetailService.getAllActiveIngredientByAdminSite(), HttpStatus.OK);
    }

    @GetMapping("/writeCSV")
    public ResponseEntity<ResponseDto> ingredientDetailWriteCSV() throws Exception{
        return new ResponseEntity<>(ingredientDetailService.ingredientDetailWriteCSV(), HttpStatus.OK);
    }

    @GetMapping("/getAllIngredientWithPagination/{searchRec}/{pageNo}/{pageCount}")
    public ResponseEntity<ResponseDto> getAllIngredientWithPagination(@PathVariable("searchRec") String searchRec, @PathVariable("pageNo") int pageNo, @PathVariable("pageCount") int pageCount) throws Exception{
        return new ResponseEntity<>(ingredientDetailService.getAllIngredientWithPagination(pageNo,pageCount,searchRec), HttpStatus.OK);
    }
}
