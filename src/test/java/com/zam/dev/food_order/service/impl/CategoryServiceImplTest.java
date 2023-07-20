package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Category;
import com.zam.dev.food_order.model.admin.CategoryRequest;
import com.zam.dev.food_order.model.admin.CategoryResponse;
import com.zam.dev.food_order.model.admin.CategoryUpdateRequest;
import com.zam.dev.food_order.repository.CategoryRepository;
import com.zam.dev.food_order.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CategoryRepository categoryRepository;

    private MockMultipartFile firstFile;


    @BeforeEach
    void setUp() throws Exception {
        firstFile = new MockMultipartFile("images", "code.png", "image/png", resourceLoader.getResource("classpath:code.png").getInputStream());
        categoryRepository.deleteAll();
    }

    @Test
    void testListCategory() {
        PageImpl<CategoryResponse> responsePage = categoryService.findAll(0, 1);
        assertNotNull(responsePage.getContent());
        assertEquals(0, responsePage.getContent().size());
        assertEquals(0, responsePage.getNumber());
    }

    @Test
    void testAddCategory() throws IOException {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("Makanan Basah");
        CategoryResponse response = categoryService.add(categoryRequest, firstFile);
        assertNotNull(response.getId());
        assertNotNull(response.getName());
        assertNotNull(response.getImages());
    }

    @Test
    void testPickImage() throws IOException {
        log.info(firstFile.getName());
        log.info(firstFile.getOriginalFilename());
        log.info("size , {}", firstFile.getSize());
    }

    @Test
    void testUpdateSuccess() throws IOException {
        Category category = new Category();
        category.setId("1");
        category.setName("category");
        category.setImages("image");
        categoryRepository.save(category);

        CategoryUpdateRequest request = new CategoryUpdateRequest();
        request.setId(category.getId());
        request.setName("new name");

        CategoryResponse categoryResponse = categoryService.update(request, firstFile);
        assertNotNull(categoryResponse);
        assertEquals("1", categoryResponse.getId());
        assertEquals("new name", categoryResponse.getName());
    }


    @Test
    void testDeleteSuccess() {
        Category category = new Category();
        category.setId("1");
        category.setName("category");
        category.setImages("image");
        categoryRepository.save(category);
        int delete = categoryService.delete("1");
        assertEquals(1, delete);
    }

    @Test
    void testDeleteFailed() {
        assertThrows(ResponseStatusException.class, () -> {
                    categoryService.delete("12");
                }
        );
    }

    @AfterEach
    void destroy() {
        categoryRepository.deleteAll();
    }

}