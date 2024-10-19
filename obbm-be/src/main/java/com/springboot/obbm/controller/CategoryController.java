package com.springboot.obbm.controller;

import com.springboot.obbm.dto.category.request.CategoryRequest;
import com.springboot.obbm.dto.category.response.CategoryResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    private CategoryService categoryService;

    @GetMapping
    ApiResponse<PageImpl<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try{
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<CategoryResponse>>builder()
                    .result(categoryService.getAllCategories(adjustedPage,size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    ApiResponse<CategoryResponse> getCategory(@PathVariable int id) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.getCategoryById(id))
                .build();
    }

    @PostMapping
    ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<CategoryResponse> updateCategory(@PathVariable int id, @RequestBody CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.updateCategory(id,request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ApiResponse.builder()
                .message("Danh mục đã bị xóa.")
                .build();
    }

}
