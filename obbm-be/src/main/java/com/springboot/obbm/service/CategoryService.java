package com.springboot.obbm.service;

import com.springboot.obbm.dto.category.request.CategoryRequest;
import com.springboot.obbm.dto.category.response.CategoryResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.CategoryMapper;
import com.springboot.obbm.models.Category;
import com.springboot.obbm.respository.CategoryRespository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryService {
    CategoryRespository categoryRespository;
    CategoryMapper categoryMapper;

    public PageImpl<CategoryResponse> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryRespository.findAllByDeletedAtIsNull(pageable);

        var responseList = categoryPage.getContent().stream()
                .distinct()
                .map(categoryMapper::toCategoryResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, categoryPage.getTotalElements());
    }

    public CategoryResponse getCategoryById(int id) {
        return categoryMapper.toCategoryResponse(categoryRespository.findByCategoryIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
    }

    public CategoryResponse createCategory(CategoryRequest request){
        Category category = categoryMapper.toCategory(request);
        category.setCreatedAt(LocalDateTime.now());
        return categoryMapper.toCategoryResponse(categoryRespository.save(category));
    }

    public CategoryResponse updateCategory(int id, CategoryRequest request){
        Category category = categoryRespository.findByCategoryIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        category.setUpdatedAt(LocalDateTime.now());

        categoryMapper.updateCaterogy(category, request);
        return categoryMapper.toCategoryResponse(categoryRespository.save(category));
    }

    public void deleteCategory(int id) {
        Category category = categoryRespository.findByCategoryIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        category.setDeletedAt(LocalDateTime.now());
        categoryRespository.save(category);
    }
}
