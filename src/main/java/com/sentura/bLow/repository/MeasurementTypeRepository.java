package com.sentura.bLow.repository;

import com.sentura.bLow.entity.MeasurementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementTypeRepository extends JpaRepository<MeasurementType,Long> {

    List<MeasurementType> findMeasurementTypeByIsActiveTrue();
}
