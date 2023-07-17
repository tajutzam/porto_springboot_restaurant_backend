package com.zam.dev.food_order.service;

import com.zam.dev.food_order.entity.Category;
import com.zam.dev.food_order.model.CategoryRequest;
import com.zam.dev.food_order.model.CategoryResponse;
import com.zam.dev.food_order.model.CategoryUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    PageImpl<CategoryResponse> findAll(int page , int size);

    CategoryResponse add(CategoryRequest categoryRequest  , MultipartFile multipartFile) ;

    CategoryResponse findById(String id);

    CategoryResponse update(CategoryUpdateRequest request , MultipartFile multipartFile);

    int delete(String id);

    CategoryResponse castToCategoryResponse(Category category);
}
