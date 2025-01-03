package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "user_package")
public class UserPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPackageId;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserDetail userDetail;

    @ManyToOne(cascade = CascadeType.ALL)
    private PackageDetail packageDetail;
    private String subscriptionId;
    private Double packagePrice;
    private Integer validDayCount;
    private Integer trialDayCount;
    private Date startDate;
    private Date expireDate;
    private Boolean isActive;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDateTime = new Date();

    @Override
    public String toString() {
        return "UserPackage{" +
                "userPackageId=" + userPackageId +
                ", userDetail=" + userDetail +
                ", packageDetail=" + packageDetail +
                ", packagePrice=" + packagePrice +
                ", validDayCount=" + validDayCount +
                ", startDate=" + startDate +
                ", expireDate=" + expireDate +
                ", isActive=" + isActive +
                ", createDate=" + createDateTime +
                '}';
    }
}
