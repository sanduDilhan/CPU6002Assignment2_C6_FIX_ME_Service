package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "daily_carb_limit")
public class DailyCarbLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyCarbLimitId;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserDetail userDetail;

    private Double carbLimit;
    private Boolean isActive;
    private Date createDate;

    @Override
    public String toString() {
        return "DailyCarbLimit{" +
                "dailyCarbLimitId=" + dailyCarbLimitId +
                ", userDetail=" + userDetail +
                ", carbLimit=" + carbLimit +
                ", isActive=" + isActive +
                ", createDate=" + createDate +
                '}';
    }
}
