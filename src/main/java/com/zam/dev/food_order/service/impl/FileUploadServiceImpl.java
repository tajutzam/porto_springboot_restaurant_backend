package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.properties.FileProperties;
import com.zam.dev.food_order.service.FileUploadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


@Service
@Slf4j
@AllArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private ResourceLoader resourceLoader;

    @Override
    public String upload(MultipartFile multipartFile, String categoryImage, String pathFile) {
        if (multipartFile != null) {
            if (multipartFile.getSize() == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please pick image");
            }
            if(Objects.equals(multipartFile.getContentType(), MediaType.IMAGE_JPEG_VALUE) || Objects.equals(multipartFile.getContentType(), MediaType.IMAGE_PNG_VALUE)){
                try {
                    String cleanName = cleanName(categoryImage, Objects.requireNonNull(multipartFile.getContentType()));
                    URL resource = resourceLoader.getResource("classpath:" + pathFile).getURL();
                    String uploadPath = resource.getPath() + cleanName;
                    log.info(uploadPath);
                    multipartFile.transferTo(Path.of(uploadPath));
                    return cleanName;
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
                }
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Your image format is not allowed");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "image is empty");
    }
    @Override
    public String cleanName(String category, String type) {
        String[] splitType = type.split("/");
        return System.currentTimeMillis() + category + "." + splitType[1];
    }


}
