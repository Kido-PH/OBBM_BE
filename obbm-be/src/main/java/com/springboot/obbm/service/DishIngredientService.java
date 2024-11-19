package com.springboot.obbm.service;

import com.springboot.obbm.dto.dishingredient.request.DishIngredientRequest;
import com.springboot.obbm.dto.dishingredient.response.DishIngredientResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.DishIngredientMapper;
import com.springboot.obbm.model.*;
import com.springboot.obbm.respository.DishIngredientRepository;
import com.springboot.obbm.respository.DishRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DishIngredientService {
    DishIngredientRepository dishIngredientRepository;
    DishRepository dishRepository;
    IngredientRepository ingredientRepository;
    DishIngredientMapper dishIngredientMapper;

    public List<DishIngredientResponse> saveAllDishIngredient(List<DishIngredientRequest> requestList) {
        List<DishIngredient> dishIngredientList = new ArrayList<>();
        for (DishIngredientRequest request : requestList) {
            Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Nguyên liệu"));
            Dish dish = dishRepository.findById(request.getDishId())
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Món ăn"));
            DishIngredient dishIngredient = dishIngredientMapper.toDishIngredient(request);
            dishIngredient.setCreatedAt(LocalDateTime.now());
            dishIngredient.setDishes(dish);
            dishIngredient.setIngredients(ingredient);

            dishIngredientList.add(dishIngredient);
        }

        return dishIngredientMapper.toDishIngredientResponseList(dishIngredientRepository.saveAll(dishIngredientList));
    }

    public PageImpl<DishIngredientResponse> getAllDishIngredient(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DishIngredient> dishIngredientPage = dishIngredientRepository.findAllByDeletedAtIsNull(pageable);

        var responseList = dishIngredientPage.getContent().stream()
                .distinct().map(dishIngredientMapper::toDishIngredientResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, dishIngredientPage.getTotalElements());
    }

    public DishIngredientResponse getDishIngredientById(int id) {
        return dishIngredientMapper.toDishIngredientResponse(dishIngredientRepository.findByDishingredientIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Nguyên liệu món ăn")));
    }

    public PageImpl<DishIngredientResponse> getDishIngredientByDishId(int menuId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DishIngredient> DishIngredientPage = dishIngredientRepository.findAllByDishes_DishIdAndDeletedAtIsNull(menuId, pageable);

        var responseList = DishIngredientPage.getContent().stream()
                .map(dishIngredientMapper::toDishIngredientResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, DishIngredientPage.getTotalElements());
    }

    public PageImpl<DishIngredientResponse> getDishIngredientByIngredientId(int dishId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DishIngredient> DishIngredientPage = dishIngredientRepository.findAllByIngredients_IngredientIdAndDeletedAtIsNull(dishId, pageable);

        var responseList = DishIngredientPage.getContent().stream()
                .map(dishIngredientMapper::toDishIngredientResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, DishIngredientPage.getTotalElements());
    }

    public DishIngredientResponse createDishIngredient(DishIngredientRequest request) {
        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Nguyên liệu"));
        Dish dish = dishRepository.findById(request.getDishId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Món ăn"));
        DishIngredient dishIngredient = dishIngredientMapper.toDishIngredient(request);
        dishIngredient.setCreatedAt(LocalDateTime.now());
        dishIngredient.setDishes(dish);
        dishIngredient.setIngredients(ingredient);
        return dishIngredientMapper.toDishIngredientResponse(dishIngredientRepository.save(dishIngredient));
    }

    public DishIngredientResponse updateDishIngredient(int id, DishIngredientRequest request) {
        DishIngredient DishIngredient = dishIngredientRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Nguyên liệu món ăn"));
        Dish dish = dishRepository.findById(request.getDishId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Món ăn"));
        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Nguyên liệu"));
        DishIngredient.setUpdatedAt(LocalDateTime.now());
        DishIngredient.setDishes(dish);
        DishIngredient.setIngredients(ingredient);
        dishIngredientMapper.updateDishIngredient(DishIngredient, request);
        return dishIngredientMapper.toDishIngredientResponse(dishIngredientRepository.save(DishIngredient));
    }

    public void deleteDishIngredient(int id) {
        DishIngredient DishIngredient = dishIngredientRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Nguyên liệu món ăn"));

        DishIngredient.setDeletedAt(LocalDateTime.now());
        dishIngredientRepository.save(DishIngredient);
    }
}
