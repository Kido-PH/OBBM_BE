package com.springboot.obbm.service;

import com.springboot.obbm.dto.dish.request.DishRequest;
import com.springboot.obbm.dto.dish.response.DishResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.DishMapper;
import com.springboot.obbm.model.Category;
import com.springboot.obbm.model.Dish;
import com.springboot.obbm.respository.CategoryRespository;
import com.springboot.obbm.respository.DishRespository;
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
public class DishService {
    DishRespository dishRespository;
    CategoryRespository categoryRespository;
    DishMapper dishMapper;

    public PageImpl<DishResponse> getAllDishes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRespository.findAllByDeletedAtIsNull(pageable);

        var responseList = dishPage.getContent().stream()
                .distinct().map(dishMapper::toDishResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, dishPage.getTotalElements());
    }

    public DishResponse getDishById(int id) {
        return dishMapper.toDishResponse(dishRespository.findByDishIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Món ăn")));
    }

    public DishResponse createDish(DishRequest request){
        Dish dish = dishMapper.toDish(request);
        Category category = categoryRespository.findById(request.getCategoryId())
                .orElseThrow( () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Danh mục"));
        dish.setCategories(category);
        dish.setCreatedAt(LocalDateTime.now());

        return dishMapper.toDishResponse(dishRespository.save(dish));
    }

    public DishResponse updateDish(int id, DishRequest request){
        Dish dish = dishRespository.findByDishIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Món ăn"));
        Category category = categoryRespository.findById(request.getCategoryId())
                .orElseThrow( () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Danh mục"));
        dish.setCategories(category);
        dish.setCreatedAt(LocalDateTime.now());

        dishMapper.upadteDish(dish, request);
        return dishMapper.toDishResponse(dishRespository.save(dish));
    }

    public void deleteDish(int id) {
        Dish dish = dishRespository.findByDishIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Món ăn"));

        dish.setDeletedAt(LocalDateTime.now());
        dishRespository.save(dish);
    }
}
