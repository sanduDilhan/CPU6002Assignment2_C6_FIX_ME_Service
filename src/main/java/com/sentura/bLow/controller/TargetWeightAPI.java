package com.sentura.bLow.controller;

import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.dto.TargetWeightDTO;
import com.sentura.bLow.service.TargetWeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/weightTracker")
public class TargetWeightAPI {



    @Autowired
    private TargetWeightService targetWeightService;

    @PostMapping("/saveTargetOrActualWeight")
    public ResponseEntity<ResponseDto> saveTargetWeight(@Valid @RequestBody TargetWeightDTO targetWeightDTO) throws Exception{
        return new ResponseEntity<>(targetWeightService.setTargetOrActualWeight(targetWeightDTO), HttpStatus.OK);
    }

    @GetMapping("/activeTargetWeight/{userId}")
    public ResponseEntity<ResponseDto> getActiveTargetWeight(@PathVariable("userId")Long userId) throws Exception{
        return new ResponseEntity<>(targetWeightService.getActiveTargetWeight(userId), HttpStatus.OK);
    }

    @GetMapping("/actualWeightHistory/{userId}")
    public ResponseEntity<ResponseDto> getActualWeightHistory(@PathVariable("userId")Long userId) throws Exception{
        return new ResponseEntity<>(targetWeightService.getWeightTrackerHistory(userId), HttpStatus.OK);
    }

    @GetMapping("/weightTrackerChart/{userId}")
    public ResponseEntity<ResponseDto> getWeightTrackerChart(@PathVariable("userId")Long userId) throws Exception{
        return new ResponseEntity<>(targetWeightService.getWeightTracker(userId), HttpStatus.OK);
    }
}
