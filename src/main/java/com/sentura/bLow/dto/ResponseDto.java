package com.sentura.bLow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ResponseDto {

    private String code = "200";
    private String timestamp = new Date().toString();
    private Object result;
    private String desc;

    private Long totalElements;
    private Integer totalPages;

    public ResponseDto(Object result) {
        this.result = result;
    }

    public ResponseDto(String code, Object result) {
        this.code = code;
        this.result = result;
    }

    public ResponseDto(String desc, String code, Object result) {
        this.desc = desc;
        this.code = code;
        this.result = result;
    }

    public ResponseDto(String desc, String code, Long totalElements, Integer totalPages, Object result) {
        this.desc = desc;
        this.code = code;
        this.totalElements = totalElements;
        this.totalPages =  totalPages;
        this.result = result;
    }
}