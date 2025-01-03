package com.sentura.bLow.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseHeader {

    private String code;
    private String message;
    @JsonIgnore
    private String responseCode;

    public ResponseHeader(String code, String message, String responseCode){
        this.code=code;
        this.message=message;
        this.responseCode=responseCode;
    }
}
