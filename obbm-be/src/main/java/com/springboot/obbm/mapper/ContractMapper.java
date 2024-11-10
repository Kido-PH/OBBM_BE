package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.contract.request.ContractRequest;
import com.springboot.obbm.dto.contract.response.ContractResponse;
import com.springboot.obbm.model.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "menus", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "users", ignore = true)
    Contract toContract(ContractRequest request);
    ContractResponse toContractResponse(Contract contract);
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "menus", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "users", ignore = true)
    void updateContract(@MappingTarget Contract contract, ContractRequest  request);
}
