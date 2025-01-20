package com.sentura.bLow.controller;

import com.sentura.bLow.dto.MeasurementTypeDTO;
import com.sentura.bLow.dto.PackageDetailDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.service.MeasurementTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/measurement")
public class MeasurementTypeAPI {

    @Autowired
    private MeasurementTypeService measurementTypeService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> create(@RequestBody MeasurementTypeDTO measurementTypeDTO) throws Exception{

        return new ResponseEntity<>(measurementTypeService.createMeasurementType(measurementTypeDTO), HttpStatus.OK);
    }

    @GetMapping("/delete/{measurementTypeId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable("measurementTypeId") Long measurementTypeId) throws Exception{

        return new ResponseEntity<>(measurementTypeService.deleteMeasurementType(measurementTypeId), HttpStatus.OK);
    }

    @GetMapping("/getAllActiveMeasurement")
    public ResponseEntity<ResponseDto> getAllActiveMeasurementType() throws Exception{

        return new ResponseEntity<>(measurementTypeService.getAllActiveMeasurementType(), HttpStatus.OK);
    }
}
