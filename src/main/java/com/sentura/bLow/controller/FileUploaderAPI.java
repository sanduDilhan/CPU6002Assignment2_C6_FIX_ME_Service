package com.sentura.bLow.controller;

import com.sentura.bLow.dto.CommonResponse;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.service.FileUploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/fileUploader")
public class FileUploaderAPI {

    @Autowired
    private FileUploaderService fileUploaderService;

    @PostMapping(value = "/save", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse> saveRecipe(
            @RequestParam(value = "image") MultipartFile image) throws Exception{
        return new ResponseEntity<>(fileUploaderService.saveFile(image), HttpStatus.OK);
    }
}
