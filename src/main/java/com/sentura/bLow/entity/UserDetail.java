package com.sentura.bLow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "user_detail")
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String stripeCustomerId;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String userRole;
    private String verifyCode;
    private Boolean verified;
    private Boolean isActive;
    private Boolean isAdminCreate;
    private Date createDate;

    @Override
    public String toString() {
        return "UserDetail{" +
                "userId=" + userId +
                ", stripeCustomerId='" + stripeCustomerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userRole='" + userRole + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", verified=" + verified +
                ", isActive=" + isActive +
                ", isAdminCreate=" + isAdminCreate +
                ", createDate=" + createDate +
                '}';
    }
}
