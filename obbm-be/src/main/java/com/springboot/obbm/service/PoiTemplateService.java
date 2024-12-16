package com.springboot.obbm.service;

import com.deepoove.poi.XWPFTemplate;
import com.springboot.obbm.dto.contract.response.ContractResponse;
import com.springboot.obbm.respository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PoiTemplateService {
    UserRepository userRepository;
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
        content.put("ngayTao", formatNgay(LocalDateTime.now()));
        content.put("HD_HopDongId", contract.getContractId() + 10000);
        content.put("HD_TenHopDong", contract.getName().toUpperCase());

        //Khách hàng
        content.put("HD_TenKH", contract.getName());
        content.put("HD_DiaDiemKH", contract.getUsers().getResidence());
        content.put("HD_SdtKH", contract.getCustphone());
        content.put("HD_EmailKH", contract.getCustmail());

        //Điều 2. Thông tin tiệc
//        content.put("HD_EmailKH", contract.get());
        return content;
    }

    private String formatNgay(LocalDateTime dateTime) {
        return DateTimeFormatter.ofPattern("'ngày' dd 'tháng' MM 'năm' yyyy").toString();
    }
}
