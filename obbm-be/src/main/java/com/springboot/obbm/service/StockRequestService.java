package com.springboot.obbm.service;

import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.model.*;
import com.springboot.obbm.respository.StockRequestRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StockRequestService {
    StockRequestRepository stockRequestRepository;

    public void generateStockRequestsForContract(Contract contract) {
        Menu menu = contract.getMenus();
        if (menu == null || menu.getListMenuDish().isEmpty()) {
            throw new AppException(ErrorCode.MENUDISH_NOT_EXISTED);
        }

        Map<Ingredient, Integer> ingredientQuantities = new HashMap<>();

        for (MenuDish menuDish : menu.getListMenuDish()) {
            Dish dish = menuDish.getDishes();
            int dishQuantity = menuDish.getQuantity();

            for (DishIngredient dishIngredient : dish.getListDishIngredient()) {
                Ingredient ingredient = dishIngredient.getIngredients();
                int requiredQuantity = Integer.parseInt(dishIngredient.getQuantity()) * dishQuantity;

                ingredientQuantities.merge(ingredient, requiredQuantity, Integer::sum);
            }
        }

        for (Map.Entry<Ingredient, Integer> entry : ingredientQuantities.entrySet()) {
            StockRequest stockRequest = StockRequest.builder()
                    .contracts(contract)
                    .ingredients(entry.getKey())
                    .quantity(entry.getValue())
                    .approval("Pending Approval")
                    .requestdate(LocalDateTime.now())
                    .status("New")
                    .build();
            stockRequestRepository.save(stockRequest);
        }
    }
}
