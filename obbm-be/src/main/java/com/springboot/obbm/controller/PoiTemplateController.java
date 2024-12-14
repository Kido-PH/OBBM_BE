package com.springboot.obbm.controller;

import com.deepoove.poi.XWPFTemplate;
import com.springboot.obbm.dto.contract.response.ContractResponse;
import com.springboot.obbm.service.ContractService;
import com.springboot.obbm.service.PoiTemplateService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/word/template")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PoiTemplateController {
    ContractService contractService;
    PoiTemplateService poiTemplateService;

    @GetMapping("/contract")
    public void downloadBienBanHop(HttpServletResponse response, int contractId) {
        try {
            ContractResponse contract = contractService.getContractById(contractId);

            String abbreviation = Arrays.stream(contract.getName().split(" "))
                    .map(word -> word.substring(0, 1).toUpperCase())
                    .collect(Collectors.joining());

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formattedDate = contract.getCreatedAt().format(outputFormatter);

            String fileName = "HopDong_" + abbreviation + "_" + formattedDate + ".docx";

            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

            XWPFTemplate document = poiTemplateService.generateContractWordXWPFTemplate(contractId);

            OutputStream os = response.getOutputStream();
            document.write(os);
            os.close();
        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }
    }

}
