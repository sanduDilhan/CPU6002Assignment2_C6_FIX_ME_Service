package com.sentura.bLow.service;

import com.sentura.bLow.dto.CommonResponse;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.dto.ResponseHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FileUploaderService {

    @Autowired
    private ImageUploadService imageUploadService;

    public CommonResponse saveFile(MultipartFile image) throws Exception {
        String[] results = imageUploadService.getResultsOfFileWrite(image);
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setCode("EVNT1000");
        responseHeader.setMessage("Success");
        responseHeader.setResponseCode("200");
        return new CommonResponse(responseHeader, results[1]);
    }
}
