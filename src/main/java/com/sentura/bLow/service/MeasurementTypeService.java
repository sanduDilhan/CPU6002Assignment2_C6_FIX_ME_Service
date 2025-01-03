package com.sentura.bLow.service;

import com.sentura.bLow.dto.MeasurementTypeDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.entity.MeasurementType;
import com.sentura.bLow.repository.MeasurementTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MeasurementTypeService {

    @Autowired
    private MeasurementTypeRepository measurementTypeRepository;

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto createMeasurementType(MeasurementTypeDTO measurementTypeDTO)throws Exception{
        if (measurementTypeDTO.getMeasurement() == null && measurementTypeDTO.getMeasurementName() == null){
            return new ResponseDto("failed", "500", "Measure name or measure size is required");
        }else {
            MeasurementType measurementType = new MeasurementType();
            measurementType.setMeasurementTypeId(0L);
            if (measurementTypeDTO.getMeasurementName() == null){
                measurementType.setMeasurementName("");
            }else {
                measurementType.setMeasurementName(measurementTypeDTO.getMeasurementName());
            }
            if (measurementTypeDTO.getMeasurement() == null){
                measurementType.setMeasurement(0.0);
            }else {
                measurementType.setMeasurement(measurementTypeDTO.getMeasurement());
            }
            measurementType.setIsActive(true);

            measurementTypeRepository.save(measurementType);
            return new ResponseDto("success", "200", null);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto deleteMeasurementType(Long measurementTypeId)throws Exception{
        MeasurementType measurementType = measurementTypeRepository.findById(measurementTypeId).get();
        if(measurementType != null){
            measurementType.setIsActive(false);
            measurementTypeRepository.save(measurementType);
            return new ResponseDto("success", "200", null);
        }else{
            return new ResponseDto("failed", "500", "Please enter valid measurement type");
        }
    }

    public ResponseDto getAllActiveMeasurementType()throws Exception{
        List<MeasurementType> measurementTypeList = measurementTypeRepository.findMeasurementTypeByIsActiveTrue();
        List<MeasurementTypeDTO> measurementTypeDTOList = new ArrayList<>();
        for (MeasurementType measurementType: measurementTypeList){
            MeasurementTypeDTO measurementTypeDTO = new MeasurementTypeDTO();
            measurementTypeDTO.setMeasurementTypeId(measurementType.getMeasurementTypeId());
            measurementTypeDTO.setMeasurementName(measurementType.getMeasurementName());
            measurementTypeDTO.setMeasurement(measurementType.getMeasurement());
            measurementTypeDTO.setIsActive(measurementType.getIsActive());
            measurementTypeDTO.setCreateDate(measurementType.getCreateDate());

            measurementTypeDTOList.add(measurementTypeDTO);
        }
        return new ResponseDto("success", "200", measurementTypeDTOList);
    }
}
