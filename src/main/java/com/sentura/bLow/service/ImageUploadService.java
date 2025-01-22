package com.sentura.bLow.service;

import com.sentura.bLow.util.FileUtilizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@Service
public class ImageUploadService {
    @Value("${archive.path}")
    private String archivePath;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${server.file.prefix}")
    private String filePrefix;

    public String[] getResultsOfFileWrite(MultipartFile imageFile) throws Exception{
        String urlPrefix = serverUrl + "/" + filePrefix + "/";
        String fileName = new FileUtilizer().generateFileName(imageFile.getOriginalFilename());

        if (!new FileUtilizer().writeToDisk(imageFile, Paths.get(archivePath), fileName)) {
            throw new Exception("File Writing Error Occurred!");
        } else {
            return new String[]{(archivePath + "/" + fileName),(urlPrefix + fileName),(fileName)};
        }
    }

}
