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

    ChatLanguageModel chatLanguageModel; // Tích hợp ChatGPT API qua LangChain4J
    DishRepository dishRepository;

    public List<MenuAIResponse> generateMenu(Integer eventId, double budget)  {
        // Lấy danh sách món ăn hợp lệ theo sự kiện và ngân sách
        List<Dish> validDishes = fetchValidDishes(eventId, budget);

        // Tạo prompt gửi đến AI
        String prompt = createPrompt(validDishes, eventId, budget);

        // Gọi API của AI để tạo menu
        String aiResponse = chatLanguageModel.generate(prompt);

        // Phân tích và xác minh kết quả từ AI
        return parseAndValidateMenus(aiResponse, eventId, budget);
    }

    private List<Dish> fetchValidDishes(Integer eventId, double budget) {
        List<Dish> dishes = dishRepository.findByEventId(eventId);
        if (dishes.isEmpty()) {
            log.warn("Không tìm thấy món ăn cho sự kiện: {}", eventId);
            throw new IllegalArgumentException("Không có món ăn khả dụng cho sự kiện đã chọn.");
        }

        // Lọc món ăn phù hợp với ngân sách
        List<Dish> validDishes = dishes.stream()
                .filter(dish -> dish.getPrice() <= budget)
                .collect(Collectors.toList());

        if (validDishes.isEmpty()) {
            log.warn("Không có món ăn phù hợp với ngân sách: {}", budget);
            throw new IllegalArgumentException("Không có món ăn khả dụng trong khoảng ngân sách đã chọn.");
        }
        return validDishes;
    }

    private String createPrompt(List<Dish> dishes, Integer eventId, double budget) {
        StringBuilder dishList = new StringBuilder();
        dishes.forEach(dish -> dishList.append(String.format("- %d: %s (%.2f)\n",
                dish.getDishId(), dish.getName(), dish.getPrice())));

        return String.format(
                """
                You are an AI assistant tasked with creating menus for an event.
                
                **Event Details:**
                - Event ID: %s
                - Target budget per menu: %.2f (Each menu **must** be within ±10,000 of this budget).
                
                **Available Dishes:**
                %s
                
                **Instructions for Menu Creation:**
                - Create **exactly 2 menus**.
                - For each menu, start adding dishes from the list, one by one, starting with the first dish.
                - Stop adding dishes once the total cost exceeds the upper limit of the budget range (%.2f).
                - Only include dishes that are summed up before the total exceeds the budget. The menu should be finalized once the budget limit is reached.
                - Ensure each menu's total cost falls within the range [%.2f, %.2f].
                - Minimize overlap of dishes between the two menus to ensure diversity.
                
                **Response Requirements:**
                - The response **must** be valid JSON with the following structure:
                {
                  "menus": [
                    {
                      "listDish": [
                        { "dishId": 1, "name": "", "price": 0.0 }
                      ],
                      "totalCost": 0.0
                    },
                    {
                      "listDish": [
                        { "dishId": 50, "name": "", "price": 0.0 }
                      ],
                      "totalCost": 0.0
                    }
                  ]
                }
                
                **Important Notes:**
                - Menus outside the budget range will be rejected.
                - Invalid JSON will be rejected.
                - Focus on summing the dish prices incrementally. If adding a dish exceeds the upper limit of the budget, stop and finalize the menu.
                - Aim for maximum diversity between the two menus by minimizing repeated dishes.
                """,
                eventId, budget, dishList, budget + 10000, budget - 10000, budget + 10000
        );
    }

    private List<MenuAIResponse> parseAndValidateMenus(String aiResponse,  Integer eventId, double budget) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(aiResponse);
            JsonNode menuNodes = rootNode.get("menus");

            if (menuNodes == null || !menuNodes.isArray()) {
                log.warn("Phản hồi AI không hợp lệ: thiếu trường 'menus'.");
                throw new IllegalArgumentException("Phản hồi AI không chứa 'menus'.");
            }

            List<MenuAIResponse> menus = new ArrayList<>();
            for (JsonNode menuNode : menuNodes) {
                MenuAIResponse menu = parseSingleMenu(menuNode, eventId, budget);
                menus.add(menu);
            }

            return menus;
        } catch (JsonProcessingException e) {
            log.error("Không thể phân tích phản hồi AI.", e);
            throw new RuntimeException("Lỗi phân tích phản hồi từ AI.");
        }
    }

    private MenuAIResponse parseSingleMenu(JsonNode menuNode, Integer eventId, double budget) {
        Set<Integer> existingDishIds = new HashSet<>();
        List<MenuAIResponse.DishAIResponse> dishResponses = new ArrayList<>();
        double totalCost = 0;

        JsonNode dishes = menuNode.get("listDish");
        if (dishes != null && dishes.isArray()) {
            for (JsonNode dishNode : dishes) {
                int dishId = dishNode.get("dishId").asInt();
                if (existingDishIds.contains(dishId)) {
                    log.warn("Món ăn trùng lặp (dishId={}): bỏ qua.", dishId);
                    continue;
                }

                String name = dishNode.get("name").asText();
                double price = dishNode.get("price").asDouble();
                existingDishIds.add(dishId);

                // Truy vấn thông tin danh mục từ cơ sở dữ liệu
                Optional<Dish> dishOptional = dishRepository.findByDishIdAndDeletedAtIsNull(dishId);
                String category = dishOptional.map(d -> d.getCategories().getName()).orElse("Unknown");

                totalCost += price;
                dishResponses.add(new MenuAIResponse.DishAIResponse(dishId, name, price, category));
            }
        }

        // Xác minh tổng chi phí và thêm món nếu cần
        if (totalCost < budget) {
            log.info("Total cost ({}) thấp hơn mức tối thiểu của ngân sách. Đang thêm món ăn bổ sung...", totalCost);

            // Sử dụng eventId để lấy danh sách món hợp lệ
            List<Dish> validDishes = fetchValidDishes(eventId, budget); // Truyền đúng eventId
            validDishes.sort((d1, d2) -> Double.compare(d2.getPrice(), d1.getPrice())); // Sắp xếp giảm dần

            // Tìm và thêm các món để đạt gần budget nhất
            for (Dish dish : validDishes) {
                if (totalCost + dish.getPrice() <= budget) {
                    dishResponses.add(new MenuAIResponse.DishAIResponse(
                            dish.getDishId(),
                            dish.getName(),
                            dish.getPrice(),
                            dish.getCategories().getName()
                    ));
                    totalCost += dish.getPrice();
                }
                if (totalCost >= budget) {
                    break;
                }
            }

            if (totalCost < budget) {
                log.warn("Không thể đạt đủ ngân sách tối thiểu sau khi thêm món ăn. Tổng hiện tại: {}", totalCost);
            }
        }

        return MenuAIResponse.builder()
                .listDish(dishResponses)
                .totalCost(totalCost)
                .build();
    }
}


