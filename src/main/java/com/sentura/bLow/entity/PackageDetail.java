package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "package_detail")
public class PackageDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;
    private String packageName;
    private String description;
    private String priceId;
    private Double packagePrice;
    private Integer validDayCount;
    private Boolean isTrial;
    private Boolean isActive;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDateTime = new Date();

    @Override
    public String toString() {
        return "PackageDetail{" +
                "packageId=" + packageId +
                ", packageName='" + packageName + '\'' +
                ", description='" + description + '\'' +
                ", packagePrice=" + packagePrice +
                ", validDayCount=" + validDayCount +
                ", isActive=" + isActive +
                ", createDate=" + createDateTime +
                '}';
    }
}
