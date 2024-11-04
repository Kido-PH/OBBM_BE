package com.springboot.obbm.service;

import com.springboot.obbm.dto.service.request.ServiceRequest;
import com.springboot.obbm.dto.service.response.ServiceResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.ServiceMapper;
import com.springboot.obbm.models.Service;
import com.springboot.obbm.respository.ServiceRespository;
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
public class ServiceService {
    ServiceRespository serviceRespository;
    ServiceMapper serviceMapper;

    public PageImpl<ServiceResponse> getAllServices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Service> servicePage = serviceRespository.findAllByDeletedAtIsNull(pageable);

        var responseList = servicePage.getContent().stream()
                .distinct().map(serviceMapper::toServiceResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, servicePage.getTotalElements());
    }

    public ServiceResponse getServiceById(int id) {
        return serviceMapper.toServiceResponse(serviceRespository.findByServiceIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ")));
    }

    public ServiceResponse createService(ServiceRequest request){
        Service service = serviceMapper.toService(request);
        service.setCreatedAt(LocalDateTime.now());
        return serviceMapper.toServiceResponse(serviceRespository.save(service));
    }

    public ServiceResponse updateService(int id, ServiceRequest request){
        Service service = serviceRespository.findByServiceIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ"));
        service.setCreatedAt(LocalDateTime.now());
        serviceMapper.updateService(service, request);
        return serviceMapper.toServiceResponse(serviceRespository.save(service));
    }

    public void deleteService(int id) {
        Service service = serviceRespository.findByServiceIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ"));
        service.setDeletedAt(LocalDateTime.now());
        serviceRespository.save(service);
    }
}
