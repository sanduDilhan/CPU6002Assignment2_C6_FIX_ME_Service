package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "target_weight")
public class TargetWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long targetWeightId;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserDetail userDetail;

    private Double weight;
    private String specialNote;
    private Boolean isTargetWeight;
    private Boolean isActive;
    private Date actualWeightDate;
    private Date createDate;

    @Override
    public String toString() {
        return "TargetWeight{" +
                "targetWeightId=" + targetWeightId +
                ", userDetail=" + userDetail +
                ", weight=" + weight +
                ", specialNote='" + specialNote + '\'' +
                ", isTargetWeight=" + isTargetWeight +
                ", isActive=" + isActive +
                ", actualWeightDate=" + actualWeightDate +
                ", createDate=" + createDate +
                '}';
    }
}
