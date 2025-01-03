package com.sentura.bLow.util;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class FileUtilizer {

    private Logger logger = LoggerFactory.getLogger(FileUtilizer.class);

    public boolean writeToDisk(MultipartFile file, Path dir, String fileName) {
        try{
            Path filepath = Paths.get(dir.toString(), fileName);

            try (OutputStream os = Files.newOutputStream(filepath)) {
                os.write(file.getBytes());
            }
            return true;
        }catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String generateFileName(String originalFileName) {
        return new Random().nextInt(100000) + System.currentTimeMillis() + "." + FilenameUtils.getExtension(originalFileName);
    }
}
