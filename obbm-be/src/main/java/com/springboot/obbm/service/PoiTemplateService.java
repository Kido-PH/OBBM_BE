package com.springboot.obbm.service;

import com.deepoove.poi.XWPFTemplate;
import com.springboot.obbm.dto.contract.response.ContractResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PoiTemplateService {
    ContractService contractService;

    public XWPFTemplate generateContractWordXWPFTemplate(int contractId) {
        ContractResponse contract = contractService.getContractById(contractId);
        Map<String, Object> content = buildContent(contract);

        try (InputStream templateInputStream = new ClassPathResource("assets/ContractOBBM.docx").getInputStream()) {
            return XWPFTemplate.compile(templateInputStream).render(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> buildContent(ContractResponse contract) {
        Map<String, Object> content = new HashMap<>();
        content.put("HopDong_Id", contract.getContractId() + 10000);
        content.put("HopDong_Ten", contract.getName().toUpperCase());
        return content;
    }
}
