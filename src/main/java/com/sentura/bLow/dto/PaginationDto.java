package com.sentura.bLow.dto;

import lombok.Data;

@Data
public class PaginationDto {

    private int totalPages;
    private Object listData;
}