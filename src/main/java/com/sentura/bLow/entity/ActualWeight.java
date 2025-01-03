package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "actual_weight")
public class ActualWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actualWeightId;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserDetail userDetail;

    private Double weight;
    private String specialNote;
    private Date weightExpectedDate;
    private Date createDate;

    @Override
    public String toString() {
        return "ActualWeight{" +
                "actualWeightId=" + actualWeightId +
                ", userDetail=" + userDetail +
                ", weight=" + weight +
                ", specialNote='" + specialNote + '\'' +
                ", weightExpectedDate=" + weightExpectedDate +
                ", createDate=" + createDate +
                '}';
    }
}
