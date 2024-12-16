package com.springboot.obbm.service;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.TableStyle;
import com.springboot.obbm.dto.contract.response.ContractResponse;
import com.springboot.obbm.dto.menu.response.MenuDishForMenuResponse;
import com.springboot.obbm.dto.menu.response.MenuResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PoiTemplateService {
    ContractService contractService;
    MenuService menuService;

    public XWPFTemplate generateContractWordXWPFTemplate(int contractId) {
        ContractResponse contract = contractService.getContractById(contractId);
        Map<String, Object> content = buildContent(contract);

        try (InputStream templateInputStream = new ClassPathResource("assets/ContractOBBM.docx").getInputStream()) {
            return XWPFTemplate.compile(templateInputStream).render(content);
        } catch (IOException e) {
            throw new RuntimeException("Error generating contract template", e);
        }
    }

    private Map<String, Object> buildContent(ContractResponse contract) {
        Map<String, Object> content = new HashMap<>();
        content.put("HD_NgayTao", formatNgay(LocalDateTime.now()));
        content.put("HD_HopDongId", contract.getContractId() + 10000);
        content.put("HD_TenHopDong", contract.getName().toUpperCase());

        // Khách hàng
        content.put("HD_TenKH", contract.getName());
        content.put("HD_DiaDiemKH", contract.getUsers().getResidence());
        content.put("HD_SdtKH", contract.getCustphone());
        content.put("HD_EmailKH", contract.getCustmail());

        // Thông tin tiệc
        content.put("HD_SoBan", contract.getTable());

        long totalMenu = (long) (contract.getTotalcost() - contract.getLocations().getCost() - contract.getEvents().getTotalcost());
        content.put("HD_TongThucDon", formatCurrency(totalMenu * 1.0));
        content.put("HD_TongDichVu", formatCurrency(contract.getEvents().getTotalcost()));
        content.put("HD_SanhTiec", formatCurrency(contract.getLocations().getCost()));
        content.put("HD_TongTien", formatCurrency(contract.getTotalcost()));

        // Add Menu Table
        MenuResponse menu = menuService.getMenuById(contract.getMenus().getMenuId());
        content.put("MenuTable", createMenuTable(menu));

        return content;
    }

    private String formatNgay(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'ngày' dd 'tháng' MM 'năm' yyyy", Locale.forLanguageTag("vi-VN"));
        return dateTime.format(formatter);
    }

    private String formatCurrency(Double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormatter.format(amount);
    }

    private TableRenderData createMenuTable(MenuResponse menuResponse) {
        RowRenderData headerRow = Rows.of("STT", "DANH MỤC", "MÓN ĂN")
                .textBold().textColor("FFFFFF").bgColor("4472C4").center().create();

        TableRenderData table = Tables.create(headerRow);
        TableStyle style = new TableStyle();
        style.setWidth("100%");
        style.setColWidths(new int[]{10, 45, 45});
        table.setTableStyle(style);

        List<MenuDishForMenuResponse> menuDishList = menuResponse.getListMenuDish();
        double totalMenuPrice = 0;
        String currentCategory = null;
        int i;
        for (i = 0; i < menuDishList.size(); i++) {
            MenuDishForMenuResponse menuDish = menuDishList.get(i);
            String categoryName = menuDish.getDishes().getCategories().getName();
            String dishName = menuDish.getDishes().getName();
            double dishPrice = menuDish.getPrice();
            totalMenuPrice += dishPrice;

            // Hiển thị tên danh mục nếu danh mục thay đổi, nếu không thì giữ ô trống
            String categoryCell = categoryName.equals(currentCategory) ? "" : categoryName;
            currentCategory = categoryName;

            table.addRow(Rows.create(
                    Cells.of(String.valueOf(i + 1)).center().create(),
                    Cells.of(categoryCell).create(),
                    Cells.of(dishName).create()
            ));
        }

        // Thêm hàng tổng cộng
        RowRenderData totalRow = Rows.create(
                Cells.of(String.valueOf(i + 1)).center().create(),
                Cells.of("Tổng Cộng").center().bgColor("D9D9D9").create(),
                Cells.of(formatCurrency(totalMenuPrice)).horizontalRight().create()
        );
        table.addRow(totalRow);

        return table;
    }
}
