package com.sentura.bLow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomEmailDTO {

    private String customerName;
    private String customerEmail;
    private String emailSubject;
    private String emailBody;
}
