package com.springboot.obbm.controller;

import com.springboot.obbm.dto.pergroup.response.PerGroupResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.PerGroupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perGroup")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PerGroupController {
    PerGroupService perGroupService;

    @GetMapping
    ApiResponse<List<PerGroupResponse>> getAllPerGroup(){
        return ApiResponse.<List<PerGroupResponse>>builder()
                .result(perGroupService.getAllPerGroup())
                .build();
    }
}
