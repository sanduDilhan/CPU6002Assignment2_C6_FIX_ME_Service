package com.sentura.bLow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDetailDTO {

    private Long userId;
    private Long userPackageId;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String userRole;
    private String verifyCode;
    private Boolean isActive;
    private Boolean verified;
    private Date createDate;

    private Boolean isAdminCreate;
    private Date adminSetExpireDate;
}
