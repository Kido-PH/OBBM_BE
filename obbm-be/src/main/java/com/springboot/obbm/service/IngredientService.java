package com.springboot.obbm.service;

import com.springboot.obbm.dto.ingredient.request.IngredientRequest;
import com.springboot.obbm.dto.ingredient.response.IngredientResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.IngredientMapper;
import com.springboot.obbm.model.Ingredient;
import com.springboot.obbm.respository.IngredientRepository;
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
public class IngredientService {
    IngredientRepository ingredientRepository;
    IngredientMapper ingredientMapper;

    public PageImpl<IngredientResponse> getAllIngredients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ingredient> dishPage = ingredientRepository.findAllByDeletedAtIsNull(pageable);

        var responseList = dishPage.getContent().stream()
                .distinct().map(ingredientMapper::toIngredientResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, dishPage.getTotalElements());
    }

    public IngredientResponse getIngredientById(int id) {
        Ingredient ingredient = ingredientRepository.findByIngredientIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Nhiên liệu"));

        return ingredientMapper.toIngredientResponse(ingredient);
    }

    public IngredientResponse createIngredient(IngredientRequest request) {
        Ingredient ingredient = ingredientMapper.toIngredient(request);
        ingredient.setCreatedAt(LocalDateTime.now());
        return ingredientMapper.toIngredientResponse(ingredientRepository.save(ingredient));
    }

    public IngredientResponse updateIngredient(int id, IngredientRequest request) {
        Ingredient ingredient = ingredientRepository.findByIngredientIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Danh mục"));
        ingredient.setUpdatedAt(LocalDateTime.now());

        ingredientMapper.updateIngredient(ingredient, request);
        return ingredientMapper.toIngredientResponse(ingredientRepository.save(ingredient));
    }

    public void deleteIngredient(int id) {
        Ingredient ingredient = ingredientRepository.findByIngredientIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Danh mục"));

        ingredient.setDeletedAt(LocalDateTime.now());
        ingredientRepository.save(ingredient);
    }
}
