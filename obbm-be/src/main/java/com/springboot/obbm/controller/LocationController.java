package com.springboot.obbm.controller;

import com.springboot.obbm.dto.location.request.LocationAdminRequest;
import com.springboot.obbm.dto.location.request.LocationUserRequest;
import com.springboot.obbm.dto.location.response.LocationResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.LocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocationController {
    private LocationService locationService;

    @GetMapping
    ApiResponse<PageImpl<LocationResponse>> getAllLocation(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try{
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<LocationResponse>>builder()
                    .result(locationService.getAllLocation(adjustedPage,size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    ApiResponse<LocationResponse> getLocation(@PathVariable int id) {
        return ApiResponse.<LocationResponse>builder()
                .result(locationService.getLocationById(id))
                .build();
    }

    @PostMapping("/user")
    ApiResponse<LocationResponse> createLocationUser(@RequestBody LocationUserRequest request){
        return ApiResponse.<LocationResponse>builder()
                .result(locationService.createLocationUser(request))
                .build();
    }

    @PostMapping("/admin")
    ApiResponse<LocationResponse> createLocationAdmin(@RequestBody LocationAdminRequest request){
        return ApiResponse.<LocationResponse>builder()
                .result(locationService.createLocationAdmin(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<LocationResponse> updateCategory(@PathVariable int id, @RequestBody LocationAdminRequest request){
        return ApiResponse.<LocationResponse>builder()
                .result(locationService.updateLocation(id,request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteCategory(@PathVariable int id) {
        locationService.deleteLocation(id);
        return ApiResponse.builder()
                .message("Địa điểm đã bị xóa.")
                .build();
    }

}
