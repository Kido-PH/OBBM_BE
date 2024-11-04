package com.springboot.obbm.controller;

import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.dto.service.request.ServiceRequest;
import com.springboot.obbm.dto.service.response.ServiceResponse;
import com.springboot.obbm.service.ServiceService;
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
    private ServiceService serviceService;

    @GetMapping
    ApiResponse<PageImpl<ServiceResponse>> getAllServices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try{
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<ServiceResponse>>builder()
                    .result(serviceService.getAllServices(adjustedPage,size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    ApiResponse<ServiceResponse> getService(@PathVariable int id) {
        return ApiResponse.<ServiceResponse>builder()
                .result(serviceService.getServiceById(id))
                .build();
    }

    @PostMapping
    ApiResponse<ServiceResponse> createService(@RequestBody ServiceRequest request){
        return ApiResponse.<ServiceResponse>builder()
                .result(serviceService.createService(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ServiceResponse> updateService(@PathVariable int id, @RequestBody ServiceRequest request){
        return ApiResponse.<ServiceResponse>builder()
                .result(serviceService.updateService(id,request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteService(@PathVariable int id) {
        serviceService.deleteService(id);
        return ApiResponse.builder()
                .message("Dịch vụ đã bị xóa.")
                .build();
    }
}
