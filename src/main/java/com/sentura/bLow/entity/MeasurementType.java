package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "measurement_type")
public class MeasurementType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long measurementTypeId;

    private String measurementName;
    private Double measurement;
    private Boolean isActive;
    private Date createDate = new Date();

    @Override
    public String toString() {
        return "MeasurementType{" +
                "measurementTypeId=" + measurementTypeId +
                ", measurementName='" + measurementName + '\'' +
                ", measurement=" + measurement +
                ", isActive=" + isActive +
                ", createDate=" + createDate +
                '}';
    }
}
