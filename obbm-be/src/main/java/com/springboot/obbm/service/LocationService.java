package com.springboot.obbm.service;

import com.springboot.obbm.dto.location.request.LocationAdminRequest;
import com.springboot.obbm.dto.location.request.LocationUserRequest;
import com.springboot.obbm.dto.location.response.LocationResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.LocationMapper;
import com.springboot.obbm.model.Location;
import com.springboot.obbm.model.User;
import com.springboot.obbm.respository.LocationRespository;
import com.springboot.obbm.respository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LocationService {
    LocationRespository locationRespository;
    UserRepository userRepository;
    LocationMapper locationMapper;

    public PageImpl<LocationResponse> getAllLocation(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Location> locationPage = locationRespository.findAllByDeletedAtIsNullOrderByCreatedAtDesc(pageable);

        var responseList = locationPage.getContent().stream()
                .map(location -> {
                    location.setListContract(
                            location.getListContract().stream()
                                    .filter(dish -> dish.getDeletedAt() == null)
                                    .collect(Collectors.toList())
                    );
                    return locationMapper.toLocationResponse(location);
                })
                .distinct()
                .toList();

        return new PageImpl<>(responseList, pageable, locationPage.getTotalElements());
    }

    public LocationResponse getLocationById(int id) {
        Location location = locationRespository.findByLocationIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Địa điểm"));

        location.setListContract(
                location.getListContract().stream()
                        .filter(dish -> dish.getDeletedAt() == null)
                        .collect(Collectors.toList())
        );

        return locationMapper.toLocationResponse(location);
    }

    public LocationResponse createLocationUser(LocationUserRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        Location location = locationMapper.toLocationUser(request);
        location.setCreatedAt(LocalDateTime.now());
        location.setUsers(user);
        location.setIsCustom(true);
        location.setType("Private location");
        location.setStatus("Waiting for survey");
        return locationMapper.toLocationResponse(locationRespository.save(location));
    }

    public LocationResponse createLocationAdmin(LocationAdminRequest request) {
        Location location = locationMapper.toLocationAdmin(request);
        location.setCreatedAt(LocalDateTime.now());
        location.setIsCustom(false);
        return locationMapper.toLocationResponse(locationRespository.save(location));
    }

    public LocationResponse updateLocation(int id, LocationAdminRequest request) {
        Location location = locationRespository.findByLocationIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Địa điểm"));
        location.setUpdatedAt(LocalDateTime.now());
        locationMapper.updateLocation(location, request);
        return locationMapper.toLocationResponse(locationRespository.save(location));
    }

    public void deleteLocation(int id) {
        Location location = locationRespository.findByLocationIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Địa điểm"));

        location.setDeletedAt(LocalDateTime.now());
        locationRespository.save(location);
    }
}
