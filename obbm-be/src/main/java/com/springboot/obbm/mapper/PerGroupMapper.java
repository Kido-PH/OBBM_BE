package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.pergroup.response.PerGroupResponse;
import com.springboot.obbm.model.PerGroup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PerGroupMapper {
    PerGroupResponse toPerGroupResponse(PerGroup perGroup);
}
