package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.category.request.CategoryRequest;
import com.springboot.obbm.dto.category.response.CategoryResponse;
import com.springboot.obbm.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);
    CategoryResponse toCategoryResponse(Category category);
    void updateCategory(@MappingTarget Category category, CategoryRequest  request);
}
