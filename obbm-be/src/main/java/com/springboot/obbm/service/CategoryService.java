package com.springboot.obbm.service;

import com.springboot.obbm.dto.category.request.CategoryRequest;
import com.springboot.obbm.dto.category.response.CategoryResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.CategoryMapper;
import com.springboot.obbm.model.Category;
import com.springboot.obbm.respository.CategoryRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public PageImpl<CategoryResponse> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryRepository.findAllByDeletedAtIsNull(pageable);

        var responseList = categoryPage.getContent().stream()
                .map(category -> {
                    category.setListDish(
                            category.getListDish().stream()
                                    .filter(dish -> dish.getDeletedAt() == null)
                                    .collect(Collectors.toList())
                    );
                    return categoryMapper.toCategoryResponse(category);
                })
                .distinct()
                .toList();

        return new PageImpl<>(responseList, pageable, categoryPage.getTotalElements());
    }

    public CategoryResponse getCategoryById(int id) {
        Category category = categoryRepository.findByCategoryIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Danh mục"));

        category.setListDish(
                category.getListDish().stream()
                        .filter(dish -> dish.getDeletedAt() == null)
                        .collect(Collectors.toList())
        );

        return categoryMapper.toCategoryResponse(category);
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.toCategory(request);
        category.setCreatedAt(LocalDateTime.now());
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    public CategoryResponse updateCategory(int id, CategoryRequest request) {
        Category category = categoryRepository.findByCategoryIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Danh mục"));
        category.setUpdatedAt(LocalDateTime.now());

        categoryMapper.updateCategory(category, request);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    public void deleteCategory(int id) {
        Category category = categoryRepository.findByCategoryIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Danh mục"));

        category.setDeletedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
}
