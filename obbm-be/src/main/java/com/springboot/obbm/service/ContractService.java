package com.springboot.obbm.service;

import com.springboot.obbm.dto.category.request.CategoryRequest;
import com.springboot.obbm.dto.category.response.CategoryResponse;
import com.springboot.obbm.dto.contract.request.ContractRequest;
import com.springboot.obbm.dto.contract.response.ContractResponse;
import com.springboot.obbm.dto.dish.request.DishRequest;
import com.springboot.obbm.dto.dish.response.DishResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.ContractMapper;
import com.springboot.obbm.models.*;
import com.springboot.obbm.respository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ContractService {
    ContractRespository contractRespository;
    LocationRespository locationRespository;
    EventRespository eventRespository;
    MenuRespository menuRespository;
    UserRespository userRespository;
    ContractMapper contractMapper;

    public PageImpl<ContractResponse> getAllContracts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> contractPage = contractRespository.findAllByDeletedAtIsNull(pageable);

        var responseList = contractPage.getContent().stream()
                .distinct()
                .map(contractMapper::toContractResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, contractPage.getTotalElements());
    }

    public ContractResponse getContractById(int id) {
        return contractMapper.toContractResponse(contractRespository.findByContractIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
    }

    public ContractResponse createContract(ContractRequest request) {
        Contract contract = contractMapper.toContract(request);
        Location location = locationRespository.findById(request.getLocationId())
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_EXISTED));
        Event event = eventRespository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        Menu menu = menuRespository.findById(request.getMenuId())
                .orElseThrow(() -> new AppException(ErrorCode.MENU_NOT_EXISTED));
        User user = userRespository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        contract.setLocations(location);
        contract.setEvents(event);
        contract.setMenus(menu);
        contract.setUsers(user);
        contract.setCustname(user.getFullname());
        contract.setCustmail(user.getEmail());
        contract.setCustphone(user.getPhone());
        contract.setCreatedAt(LocalDateTime.now());

        return contractMapper.toContractResponse(contractRespository.save(contract));
    }


    public ContractResponse updateContract(int id, ContractRequest request){
        Contract contract = contractRespository.findByContractIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.CONTRACT_NOT_EXISTED));
        Location location = locationRespository.findById(request.getLocationId())
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_EXISTED));
        Event event = eventRespository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        Menu menu = menuRespository.findById(request.getMenuId())
                .orElseThrow(() -> new AppException(ErrorCode.MENU_NOT_EXISTED));
        User user = userRespository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        contract.setLocations(location);
        contract.setEvents(event);
        contract.setMenus(menu);
        contract.setUsers(user);
        contract.setCustname(user.getFullname());
        contract.setCustmail(user.getEmail());
        contract.setCustphone(user.getPhone());
        contract.setUpdatedAt(LocalDateTime.now());

        contractMapper.updateContract(contract, request);
        return contractMapper.toContractResponse(contractRespository.save(contract));
    }

    public void deleteContract(int id) {
        Contract contract = contractRespository.findByContractIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.CONTRACT_NOT_EXISTED));

        contract.setDeletedAt(LocalDateTime.now());
        contractRespository.save(contract);
    }
}
