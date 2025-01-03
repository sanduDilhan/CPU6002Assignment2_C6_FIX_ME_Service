package com.sentura.bLow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailChimpRequestDTO {
    private String email_address;
    private String status;
}
