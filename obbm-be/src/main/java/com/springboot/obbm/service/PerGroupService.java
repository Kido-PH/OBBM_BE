package com.springboot.obbm.service;

import com.springboot.obbm.dto.pergroup.response.PerGroupResponse;
import com.springboot.obbm.mapper.PerGroupMapper;
import com.springboot.obbm.respository.PerGroupRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PerGroupService {
    PerGroupRepository perGroupRepository;
    PerGroupMapper perGroupMapper;

    public List<PerGroupResponse> getAllPerGroup(){
        var perGroup = perGroupRepository.findAll();
        return perGroup.stream().map(perGroupMapper::toPerGroupResponse).toList();
    }
}
