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

    public PageImpl<DishIngredientResponse> getAllDishIngredients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DishIngredient> dishIngredientPage = dishIngredientRepository.findAllByDeletedAtIsNull(pageable);

        var responseList = dishIngredientPage.getContent().stream()
                .distinct().map(dishIngredientMapper::toDishIngredientResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, dishIngredientPage.getTotalElements());
    }

//    public DishIngredientResponse getDishIngredientById(int id) {
//        return dishIngredientMapper.toDishIngredientResponse(dishIngredientRepository.findByDishIngredientIdAndDeletedAtIsNull(id)
//                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ sự kiện")));
//    }
//
//    public PageImpl<DishIngredientResponse> getDishIngredientByEventId(int menuId, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<DishIngredients> DishIngredientPage = dishIngredientRepository.findAllByEvents_EventIdAndDeletedAtIsNull(menuId, pageable);
//
//        var responseList = DishIngredientPage.getContent().stream()
//                .map(dishIngredientMapper::toDishIngredientResponse)
//                .toList();
//        return new PageImpl<>(responseList, pageable, DishIngredientPage.getTotalElements());
//    }
//
//    public PageImpl<DishIngredientResponse> getDishIngredientByServiceId(int dishId, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<DishIngredients> DishIngredientPage = dishIngredientRepository.findAllByServices_ServiceIdAndDeletedAtIsNull(dishId, pageable);
//
//        var responseList = DishIngredientPage.getContent().stream()
//                .map(dishIngredientMapper::toDishIngredientResponse)
//                .toList();
//        return new PageImpl<>(responseList, pageable, DishIngredientPage.getTotalElements());
//    }
//
//    public DishIngredientResponse createDishIngredient(DishIngredientsRequest request) {
//        Services services = ingredientRepository.findById(request.getServiceId())
//                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ"));
//        Event event = dishRepository.findById(request.getEventId())
//                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
//        DishIngredients dishIngredients = dishIngredientMapper.toDishIngredient(request);
//        dishIngredients.setCreatedAt(LocalDateTime.now());
//        dishIngredients.setEvents(event);
//        dishIngredients.setServices(services);
//        return dishIngredientMapper.toDishIngredientResponse(dishIngredientRepository.save(dishIngredients));
//    }
//
//    public DishIngredientResponse updateDishIngredient(int id, DishIngredientsRequest request) {
//        DishIngredients DishIngredients = dishIngredientRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn món ăn"));
//        Event event = dishRepository.findById(request.getEventId())
//                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
//        Services services = ingredientRepository.findById(request.getServiceId())
//                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ"));
//        DishIngredients.setUpdatedAt(LocalDateTime.now());
//        DishIngredients.setEvents(event);
//        DishIngredients.setServices(services);
//        dishIngredientMapper.updateDishIngredient(DishIngredients, request);
//        return dishIngredientMapper.toDishIngredientResponse(dishIngredientRepository.save(DishIngredients));
//    }
//
//    public void deleteDishIngredient(int id) {
//        DishIngredients DishIngredients = dishIngredientRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn món ăn"));
//
//        DishIngredients.setDeletedAt(LocalDateTime.now());
//        dishIngredientRepository.save(DishIngredients);
//    }
}
