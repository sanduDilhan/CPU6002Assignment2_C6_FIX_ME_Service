package com.sentura.bLow.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {

    private ResponseHeader responseHeader;
    private Object data;

    public CommonResponse(ResponseHeader responseHeader, Object data){
        this.responseHeader=responseHeader;
        this.data=data;
    }
}
