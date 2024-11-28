package com.springboot.obbm.service;

import com.springboot.obbm.dto.contract.request.ContractRequest;
import com.springboot.obbm.dto.contract.request.ContractUpdateTotalCostRequest;
import com.springboot.obbm.dto.contract.response.ContractResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.ContractMapper;
import com.springboot.obbm.model.*;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ContractService {
    ContractRepository contractRepository;
    LocationRespository locationRespository;
    StockRequestService stockRequestService;
    EventRepository eventRepository;
    MenuRepository menuRepository;
    UserRepository userRepository;
    ContractMapper contractMapper;

    public PageImpl<ContractResponse> getAllContracts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> contractPage = contractRepository.findAllByDeletedAtIsNull(pageable);

        var responseList = contractPage.getContent().stream()
                .map(contract -> {
                    contract.setListStockrequests(
                            contract.getListStockrequests().stream()
                                    .filter(stockRequest -> stockRequest.getDeletedAt() == null)
                                    .collect(Collectors.toList())
                    );
                    return contractMapper.toContractResponse(contract);
                })
                .distinct()
                .toList();
        return new PageImpl<>(responseList, pageable, contractPage.getTotalElements());
    }

    public PageImpl<ContractResponse> getAllContractsByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> contractPage = contractRepository.findAllByUsers_UserIdAndDeletedAtIsNull(userId, pageable);

        var responseList = contractPage.getContent().stream()
                .map(contract -> {
                    contract.setListStockrequests(
                            contract.getListStockrequests().stream()
                                    .filter(stockRequest -> stockRequest.getDeletedAt() == null)
                                    .collect(Collectors.toList())
                    );
                    return contractMapper.toContractResponse(contract);
                })
                .distinct()
                .toList();
        return new PageImpl<>(responseList, pageable, contractPage.getTotalElements());
    }

    public ContractResponse getLatestContractByUserId(String userId) {
        Contract contract = contractRepository.findTopByUsers_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Hợp đồng"));

        contract.setListStockrequests(
                contract.getListStockrequests().stream()
                        .filter(stockRequest -> stockRequest.getDeletedAt() == null)
                        .collect(Collectors.toList())
        );

        return contractMapper.toContractResponse(contract);
    }

    public ContractResponse getContractById(int id) {
        Contract contract = contractRepository.findByContractIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Hợp đồng"));
        contract.setListStockrequests(
                contract.getListStockrequests().stream()
                        .filter(stockRequest -> stockRequest.getDeletedAt() == null)
                        .collect(Collectors.toList())
        );
        return contractMapper.toContractResponse(contract);
    }

    public ContractResponse createContract(ContractRequest request) {
        Contract contract = contractMapper.toContract(request);
        Location location = locationRespository.findById(request.getLocationId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Địa điểm"));
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        contract.setLocations(location);
        contract.setEvents(event);
        contract.setMenus(menu);
        contract.setUsers(user);
        contract.setCustname(request.getCustname());
        contract.setCustphone(request.getCustphone());
        contract.setCustmail(user.getEmail());
        contract.setPrepay(0.0);
        contract.setCreatedAt(LocalDateTime.now());

        Contract savedContract = contractRepository.save(contract);
        stockRequestService.generateStockRequestsForContract(savedContract);

        return contractMapper.toContractResponse(savedContract);
    }

    public ContractResponse updateContract(int id, ContractRequest request) {
        Contract contract = contractRepository.findByContractIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Hợp đồng"));
        Location location = locationRespository.findById(request.getLocationId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Địa điểm"));
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        contract.setLocations(location);
        contract.setEvents(event);
        contract.setMenus(menu);
        contract.setUsers(user);
        contract.setCustname(request.getCustname());
        contract.setCustphone(request.getCustphone());
        contract.setCustmail(user.getEmail());
        contract.setUpdatedAt(LocalDateTime.now());

        contractMapper.updateContract(contract, request);


        Contract savedContract = contractRepository.save(contract);
        stockRequestService.generateStockRequestsForContract(savedContract);

        return contractMapper.toContractResponse(savedContract);
    }


    public ContractResponse updateTotalCostContract(int id, ContractUpdateTotalCostRequest request) {
        Contract contract = contractRepository.findByContractIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Hợp đồng"));

        contract.setUpdatedAt(LocalDateTime.now());
        contractMapper.updateTotalContract(contract, request);

        Contract savedContract = contractRepository.save(contract);
        stockRequestService.generateStockRequestsForContract(savedContract);

        return contractMapper.toContractResponse(savedContract);
    }

    public void deleteContract(int id) {
        Contract contract = contractRepository.findByContractIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Hợp đồng"));

        contract.setDeletedAt(LocalDateTime.now());
        contractRepository.save(contract);
    }
}
