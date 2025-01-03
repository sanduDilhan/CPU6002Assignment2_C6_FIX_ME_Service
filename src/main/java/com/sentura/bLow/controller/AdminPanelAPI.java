package com.sentura.bLow.controller;

import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.service.AdminPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/adminPanel")
public class AdminPanelAPI {

    @Autowired
    private AdminPanelService adminPanelService;

    @GetMapping("/dashboardCardValues")
    public ResponseEntity<ResponseDto> getAllRecipes() throws Exception{
        return new ResponseEntity<>(adminPanelService.dashboardCount(), HttpStatus.OK);
    }

    @GetMapping("/weeklyRevenueAmount")
    public ResponseEntity<ResponseDto> weeklyRevenueAmount() throws Exception{
        return new ResponseEntity<>(adminPanelService.weeklyRevenueDayWise(), HttpStatus.OK);
    }
}
