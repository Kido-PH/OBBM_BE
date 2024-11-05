package com.springboot.obbm.controller;

import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.dto.services.request.ServicesRequest;
import com.springboot.obbm.dto.services.response.ServicesResponse;
import com.springboot.obbm.service.ServicesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceController {
    private ServicesService servicesService;

    @GetMapping
    ApiResponse<PageImpl<ServicesResponse>> getAllServices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try{
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<ServicesResponse>>builder()
                    .result(servicesService.getAllServices(adjustedPage,size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    ApiResponse<ServicesResponse> getService(@PathVariable int id) {
        return ApiResponse.<ServicesResponse>builder()
                .result(servicesService.getServiceById(id))
                .build();
    }

    @PostMapping
    ApiResponse<ServicesResponse> createService(@RequestBody ServicesRequest request){
        return ApiResponse.<ServicesResponse>builder()
                .result(servicesService.createService(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ServicesResponse> updateService(@PathVariable int id, @RequestBody ServicesRequest request){
        return ApiResponse.<ServicesResponse>builder()
                .result(servicesService.updateService(id,request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteService(@PathVariable int id) {
        servicesService.deleteService(id);
        return ApiResponse.builder()
                .message("Dịch vụ đã bị xóa.")
                .build();
    }
}
