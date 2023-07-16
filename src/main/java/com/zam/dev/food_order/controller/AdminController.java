package com.zam.dev.food_order.controller;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.model.*;
import com.zam.dev.food_order.service.AdminService;
import com.zam.dev.food_order.service.CategoryService;
import com.zam.dev.food_order.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public WebResponse<Object> get(Admin admin){
        return WebResponse.builder().data(AdminResponse.getInstance(admin)).status(HttpStatus.OK.value()).message("OK").build();
    }

    @GetMapping(value = "/users" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectPageResponse<Object> getUsers(Admin admin , @RequestParam(name = "page" , defaultValue = "0" , required = false) int page , @RequestParam(name = "size" , defaultValue = "10" , required = true) int size){
        PageImpl<UserResponse> responsePage = userService.findAll(page, size);
        List<UserResponse> responses = responsePage.getContent();
        return ObjectPageResponse.builder()
                .page(responsePage.getNumber())
                .size(responses.size())
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(responses)
                .build();
    }



    @GetMapping(path = "/category" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectPageResponse<Object> categories(Admin admin , @RequestParam(name = "page" , defaultValue = "0" , required = true) int page , @RequestParam(name = "size" , defaultValue = "10" , required = true) int size){
        PageImpl<CategoryResponse> responsePage = categoryService.findAll(page, size);
        return ObjectPageResponse.builder().
                message("OK")
                .size(responsePage.getSize())
                .page(responsePage.getNumber())
                .status(HttpStatus.OK.value())
                .data(responsePage.getContent()).
                build();
    }

    @PostMapping(path = "/category" ,
            produces = MediaType.APPLICATION_JSON_VALUE ,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<Object> addCategory(Admin admin , @RequestParam(name = "name" , required = true) String name , @RequestPart(name = "images")MultipartFile file){
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName(name);
        CategoryResponse categoryResponse = categoryService.add(categoryRequest, file);
        return WebResponse.builder().status(HttpStatus.CREATED.value()).message("CREATED").
        data(categoryResponse).
                build();
    }

    @GetMapping(path = "/category/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> findById(@PathVariable("id") String id, Admin admin){
        CategoryResponse response = categoryService.findById(id);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.OK.value()).build();
    }

    @PutMapping(path = "/category" , produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> updateCategory(Admin admin , @ModelAttribute CategoryUpdateRequest request , @RequestPart("images") MultipartFile file){
        CategoryResponse response = categoryService.update(request, file);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.OK.value()).build();
    }

    @DeleteMapping(path = "/category/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public WebResponse<Object> deleteCategoryById(Admin admin , @PathVariable("id") String id){
        int deleted = categoryService.delete(id);
        if(deleted == 1){
            return WebResponse.builder().status(HttpStatus.ACCEPTED.value()).data(deleted).message(HttpStatus.NOT_ACCEPTABLE.name()).build();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "failed to delete category");
    }
}
