package com.sentura.bLow.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsLetterSubscriptionRequestDTO {

    @Email(message = "Email is not valid")
    @NotEmpty
    private String email;
}
