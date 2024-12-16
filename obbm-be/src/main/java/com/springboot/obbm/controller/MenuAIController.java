package com.springboot.obbm.controller;

import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.dto.response.MenuAIResponse;
import com.springboot.obbm.service.AIMenuService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/AiMenu")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuAIController {
    private AIMenuService aiMenuService;

    @PostMapping("/generate")
    ApiResponse<List<MenuAIResponse>> generateMenu(@RequestParam Integer eventId, @RequestParam double budget) {
        return ApiResponse.<List<MenuAIResponse>>builder()
                .result(aiMenuService.generateMenu(eventId, budget))
                .build();
    }
}
