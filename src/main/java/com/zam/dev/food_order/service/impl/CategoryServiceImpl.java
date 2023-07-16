package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Category;
import com.zam.dev.food_order.model.CategoryRequest;
import com.zam.dev.food_order.model.CategoryUpdateRequest;
import com.zam.dev.food_order.properties.ApplicationProperties;
import com.zam.dev.food_order.properties.FileProperties;
import com.zam.dev.food_order.repository.CategoryRepository;
import com.zam.dev.food_order.model.CategoryResponse;
import com.zam.dev.food_order.service.CategoryService;
import com.zam.dev.food_order.service.FileUploadService;
import com.zam.dev.food_order.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private ResourceLoader resourceLoader;
    @Override
    public PageImpl<CategoryResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<CategoryResponse> responseList = categoryPage.getContent().stream().map(this::castToCategoryResponse).toList();
        return new PageImpl<>(responseList, pageable, categoryPage.getTotalElements());
    }

    @Override
    @Transactional
    public CategoryResponse add(CategoryRequest categoryRequest, MultipartFile multipartFile) {
        validationService.validate(categoryRequest);
        if (multipartFile == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please pick a image");
        }
        String imageCategoryName = fileUploadService.upload(multipartFile, "category", fileProperties.getCategory());
        Category category = new Category(
                UUID.randomUUID().toString(),
                categoryRequest.getName(),
                imageCategoryName
        );
        categoryRepository.save(category);
        return castToCategoryResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findById(String id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            return castToCategoryResponse(category);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "category not found");
    }

    @Override
    @Transactional
    public CategoryResponse update(CategoryUpdateRequest request, MultipartFile multipartFile) {
        validationService.validate(request);
        Category category = categoryRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "category id not found"));
        if(multipartFile != null){
            if(multipartFile.getSize() == 0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "please pick image valid");
            }
            String imageCategoryName = fileUploadService.upload(multipartFile, "category", fileProperties.getCategory());
            category.setImages(imageCategoryName);
            category.setName(request.getName());
            Category categoryUpdated = categoryRepository.save(category);
            return castToCategoryResponse(categoryUpdated);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "image can not be null");
    }

    @Override
    @Transactional
    public int delete(String id) {
        categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST ,"category id not found"));
        int deleteCategory = categoryRepository.customDeleteCategory(id);
        log.info("deleted record , {}" , deleteCategory);
        return deleteCategory;
    }

    private CategoryResponse castToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .images("http://localhost:" + applicationProperties.getPort() + "/images/categories/" + category.getImages())
                .name(category.getName())
                .build();
    }

}
