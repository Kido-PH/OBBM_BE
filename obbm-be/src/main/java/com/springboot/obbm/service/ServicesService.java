package com.springboot.obbm.service;

import com.springboot.obbm.dto.services.request.ServicesRequest;
import com.springboot.obbm.dto.services.response.ServicesResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.ServicesMapper;
import com.springboot.obbm.model.Services;
import com.springboot.obbm.respository.ServicesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ServicesService {
    ServicesRepository servicesRepository;
    ServicesMapper servicesMapper;

    public PageImpl<ServicesResponse> getAllServices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Services> servicePage = servicesRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc(pageable);

        var responseList = servicePage.getContent().stream()
                .distinct().map(servicesMapper::toServiceResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, servicePage.getTotalElements());
    }

    public ServicesResponse getServiceById(int id) {
        return servicesMapper.toServiceResponse(servicesRepository.findByServiceIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ")));
    }

    public ServicesResponse createService(ServicesRequest request){
        Services services = servicesMapper.toService(request);
        services.setCreatedAt(LocalDateTime.now());
        return servicesMapper.toServiceResponse(servicesRepository.save(services));
    }

    public ServicesResponse updateService(int id, ServicesRequest request){
        Services services = servicesRepository.findByServiceIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ"));
        services.setCreatedAt(LocalDateTime.now());
        servicesMapper.updateService(services, request);
        return servicesMapper.toServiceResponse(servicesRepository.save(services));
    }

    public void deleteService(int id) {
        Services services = servicesRepository.findByServiceIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ"));
        services.setDeletedAt(LocalDateTime.now());
        servicesRepository.save(services);
    }
}
