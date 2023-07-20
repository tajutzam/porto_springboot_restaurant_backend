package com.zam.dev.food_order.controller.admin.category;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.model.admin.CategoryRequest;
import com.zam.dev.food_order.model.admin.CategoryResponse;
import com.zam.dev.food_order.model.admin.CategoryUpdateRequest;
import com.zam.dev.food_order.model.other.ObjectPaging;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/api/admin/category")
@AllArgsConstructor
@RestController
public class CategoryController {


    private CategoryService categoryService;


    @GetMapping(path = "" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectPagingResponse<List<CategoryResponse>> categories(Admin admin , @RequestParam(name = "page" , defaultValue = "0" , required = true) int page , @RequestParam(name = "size" , defaultValue = "10" , required = true) int size){
        PageImpl<CategoryResponse> responsePage = categoryService.findAll(page, size);
        ObjectPagingResponse<List<CategoryResponse>> objectPagingResponse = new ObjectPagingResponse<>();
        objectPagingResponse.setStatus(HttpStatus.OK.value());
        objectPagingResponse.setMessage("OK");
        ObjectPaging objectPaging = ObjectPaging.builder().
                page(responsePage.getNumber())
                .size(responsePage.getSize())
                        .
                build();
        objectPagingResponse.setObjectPaging(objectPaging);
        objectPagingResponse.setData(responsePage.getContent());
        return objectPagingResponse;
    }

    @PostMapping(path = "" ,
            produces = MediaType.APPLICATION_JSON_VALUE ,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<Object> addCategory(Admin admin , @RequestParam(name = "name" , required = true) String name , @RequestPart(name = "images") MultipartFile file){
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName(name);
        CategoryResponse categoryResponse = categoryService.add(categoryRequest, file);
        return WebResponse.builder().status(HttpStatus.CREATED.value()).message("CREATED").
                data(categoryResponse).
                build();
    }

    @GetMapping(path = "/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> findById(@PathVariable("id") String id, Admin admin){
        CategoryResponse response = categoryService.findById(id);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.OK.value()).build();
    }

    @PutMapping(path = "" , produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> updateCategory(Admin admin , @ModelAttribute CategoryUpdateRequest request , @RequestPart("images") MultipartFile file){
        CategoryResponse response = categoryService.update(request, file);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.OK.value()).build();
    }

    @DeleteMapping(path = "/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public WebResponse<Object> deleteCategoryById(Admin admin , @PathVariable("id") String id){
        int deleted = categoryService.delete(id);
        if(deleted == 1){
            return WebResponse.builder().status(HttpStatus.ACCEPTED.value()).data(deleted).message(HttpStatus.NOT_ACCEPTABLE.name()).build();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "failed to delete category");
    }

}
