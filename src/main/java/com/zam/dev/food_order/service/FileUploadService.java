package com.zam.dev.food_order.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FileUploadService {

    String upload(MultipartFile multipartFile ,String categoryImage , String path) ;

    String cleanName(String category , String type);

}
