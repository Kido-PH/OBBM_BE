package com.springboot.obbm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.obbm.dto.response.MenuAIResponse;
import com.springboot.obbm.model.Dish;
import com.springboot.obbm.respository.DishRepository;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AIMenuService {

    ChatLanguageModel chatLanguageModel;
    DishRepository dishRepository;

    public List<MenuAIResponse> generateMenu(Integer eventId, double budget) {
        List<Dish> validDishes = fetchValidDishes(eventId, budget);

        String prompt = createPrompt(validDishes, eventId, budget);

        String aiResponse = chatLanguageModel.generate(prompt);

        return parseAndValidateMenus(aiResponse, eventId, budget);
    }

    private List<Dish> fetchValidDishes(Integer eventId, double budget) {
        List<Dish> dishes = dishRepository.findByEventId(eventId);
        if (dishes.isEmpty()) {
            log.error("Không có món ăn nào khả dụng cho sự kiện: {}", eventId);
            throw new IllegalArgumentException("Không có món ăn khả dụng.");
        }
        return dishes.stream()
                .filter(dish -> dish.getPrice() <= budget)
                .collect(Collectors.toList());
    }

    private String createPrompt(List<Dish> dishes, Integer eventId, double budget) {
        // Xây dựng danh sách món ăn từ danh sách dishes
        String dishList = dishes.stream()
                .map(dish -> String.format(
                        "- %d: %s (%.2f) [Category: %s]",
                        dish.getDishId(), dish.getName(), dish.getPrice(), dish.getCategories().getName()
                ))
                .collect(Collectors.joining("\n"));

        // Tạo prompt dựa trên template
        return String.format(
                """
                You are an AI assistant tasked with creating menus for an event.
    
                **Event Details:**
                - Event ID: %s
                - Target budget per menu: %.2f (Each menu **must** be within ±10,000 of this budget).
    
                **Available Dishes:**
                %s
    
                **Dish Categories and Instructions:**
                - Appetizers (Khai vị): At least 1 and at most 3 dishes.
                - Desserts (Tráng miệng): At least 1 and at most 3 dishes.
                - Drinks (Thức uống): At least 1 and at most 3 dishes.
                - Combined total of Appetizers, Desserts, and Drinks should be between 3 and 9 dishes.
                - Add Main Courses to reach the target budget without exceeding it.

                **Instructions for Menu Creation:**
                - Create **exactly 2 menus**.
                - Start by adding dishes from each category, ensuring the specified limits for appetizers, desserts, and drinks are met.
                - Once the minimum category requirements are satisfied, prioritize adding main courses to maximize the total cost, aiming to approach the budget.
                - Stop adding dishes if the total cost exceeds the upper budget range (%.2f). If adding a dish would exceed the budget, skip it.
                - Minimize dish repetition between the two menus to ensure diversity.
    
                **Response Requirements:**
                - The response **must** be valid JSON with the following structure:
                {
                  "menus": [
                    {
                      "listDish": [
                        { "dishId": 1, "name": "", "price": 0.0, "category": "" }
                      ],
                      "totalCost": 0.0
                    },
                    {
                      "listDish": [
                        { "dishId": 50, "name": "", "price": 0.0, "category": "" }
                      ],
                      "totalCost": 0.0
                    }
                  ]
                }
    
                **Important Notes:**
                - Each menu's total cost must fall within the range [%.2f, %.2f].
                - Menus that exceed the budget range or are invalid will be rejected.
                - Aim for diversity by minimizing repeated dishes between the two menus.
                - Prioritize meeting the category requirements first, then maximize the total cost by adding main courses.
                """,
                eventId, budget, dishList, budget + 10000, budget - 10000, budget + 10000
        );
    }

    private List<MenuAIResponse> parseAndValidateMenus(String aiResponse, Integer eventId, double budget) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(aiResponse);
            JsonNode menuNodes = rootNode.get("menus");

            if (menuNodes == null || !menuNodes.isArray()) {
                throw new IllegalArgumentException("Phản hồi AI không hợp lệ.");
            }

            List<MenuAIResponse> menus = new ArrayList<>();
            for (JsonNode menuNode : menuNodes) {
                menus.add(completeMenu(menuNode, eventId, budget));
            }

            return menus;
        } catch (JsonProcessingException e) {
            log.error("Lỗi khi phân tích phản hồi từ AI", e);
            throw new RuntimeException("Lỗi phản hồi từ AI.");
        }
    }

    private MenuAIResponse completeMenu(JsonNode menuNode, Integer eventId, double budget) {
        Set<Integer> existingDishIds = new HashSet<>();
        List<MenuAIResponse.DishAIResponse> dishResponses = new ArrayList<>();
        double totalCost = 0;

        // Xử lý các món đã được trả về từ AI
        totalCost = processExistingDishes(menuNode, existingDishIds, dishResponses, totalCost);

        // Bổ sung thêm món ăn để đạt ngân sách
        if (totalCost < budget) {
            List<Dish> validDishes = fetchValidDishes(eventId, budget);
            totalCost = fillRemainingBudget(validDishes, existingDishIds, dishResponses, totalCost, budget);
        }

        return MenuAIResponse.builder()
                .listDish(dishResponses)
                .totalCost(totalCost)
                .build();
    }

    private double fillRemainingBudget(List<Dish> validDishes, Set<Integer> existingDishIds,
                                       List<MenuAIResponse.DishAIResponse> dishResponses,
                                       double totalCost, double budget) {
        log.info("Bắt đầu bổ sung món ăn để đạt ngân sách...");

        List<Dish> remainingDishes = validDishes.stream()
                .filter(dish -> !existingDishIds.contains(dish.getDishId()))
                .sorted(Comparator.comparingDouble(Dish::getPrice))
                .toList();

        for (Dish dish : remainingDishes) {
            if (totalCost + dish.getPrice() <= budget) {
                dishResponses.add(new MenuAIResponse.DishAIResponse(
                        dish.getDishId(), dish.getName(), dish.getPrice(), dish.getCategories().getName()
                ));
                existingDishIds.add(dish.getDishId());
                totalCost += dish.getPrice();
                log.info("Đã bổ sung món: {} - Giá: {}", dish.getName(), dish.getPrice());
            }
            if (totalCost >= budget) break;
        }

        log.info("Ngân sách sau khi bổ sung: {}", totalCost);
        return totalCost;
    }

    private double processExistingDishes(JsonNode menuNode, Set<Integer> existingDishIds,
                                         List<MenuAIResponse.DishAIResponse> dishResponses,
                                         double totalCost) {
        int appetizerCount = 0, dessertCount = 0, drinkCount = 0;

        JsonNode dishes = menuNode.get("listDish");
        if (dishes != null && dishes.isArray()) {
            for (JsonNode dishNode : dishes) {
                int dishId = dishNode.get("dishId").asInt();
                if (existingDishIds.contains(dishId)) continue;

                String name = dishNode.get("name").asText();
                double price = dishNode.get("price").asDouble();

                String category = dishRepository.findByDishIdAndDeletedAtIsNull(dishId)
                        .map(d -> d.getCategories().getName())
                        .orElse("Unknown");

                // Kiểm soát số lượng món theo loại
                if (category.equalsIgnoreCase("Appetizers") && appetizerCount >= 3) continue;
                if (category.equalsIgnoreCase("Desserts") && dessertCount >= 3) continue;
                if (category.equalsIgnoreCase("Beverages") && drinkCount >= 3) continue;

                // Đếm số món theo loại
                if (category.equalsIgnoreCase("Appetizers")) appetizerCount++;
                if (category.equalsIgnoreCase("Desserts")) dessertCount++;
                if (category.equalsIgnoreCase("Beverages")) drinkCount++;

                existingDishIds.add(dishId);
                totalCost += price;
                dishResponses.add(new MenuAIResponse.DishAIResponse(dishId, name, price, category));
            }
        }
        return totalCost;
    }
}


