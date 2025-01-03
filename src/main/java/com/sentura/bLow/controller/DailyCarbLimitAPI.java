package com.sentura.bLow.controller;

import com.sentura.bLow.dto.DailyCarbLimitDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.service.DailyCarbLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carbLimit")
public class DailyCarbLimitAPI {

    @Autowired
    private DailyCarbLimitService dailyCarbLimitService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> save(@RequestBody DailyCarbLimitDTO dailyCarbLimitDTO) throws Exception{
        return new ResponseEntity<>(dailyCarbLimitService.createDailyCarbLimit(dailyCarbLimitDTO), HttpStatus.OK);
    }

    @GetMapping("/getCarbLimitByUserId/{userId}")
    public ResponseEntity<ResponseDto> getCarbLimitByUserId(@PathVariable("userId") Long userId) throws Exception{
        return new ResponseEntity<>(dailyCarbLimitService.getCarbLimitByUserId(userId), HttpStatus.OK);
    }
}
