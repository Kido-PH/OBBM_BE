//package com.springboot.obbm.controller;
//
//import com.deepoove.poi.XWPFTemplate;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.time.format.DateTimeFormatter;
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/word/template")
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class PoiTemplateController {
//    BienBanHopService bienBanHopService;
//    BaoGiaService baoGiaService;
//    POITemplateService poiTemplateService;
//    BaoGiaTemplateService baoGiaTemplateService;
//
//    @GetMapping("/bienbanhop")
//    public void downloadBienBanHop(HttpServletResponse response, int bienBanHopId) {
//        try {
//            BienBanHopResponse bienBanHop = bienBanHopService.getBienBanHopById(bienBanHopId);
//
//            String abbreviation = Arrays.stream(bienBanHop.getTen().split(" "))
//                    .map(word -> word.substring(0, 1).toUpperCase())
//                    .collect(Collectors.joining());
//
//            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//            String formattedDate = bienBanHop.getNgayTao().format(outputFormatter);
//
//            String fileName = "BienBanHop_" + abbreviation + "_" + formattedDate + ".docx";
//
//            response.reset();
//            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
//            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
//            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
//
//            XWPFTemplate document = poiTemplateService.generateBienBanHopWordXWPFTemplate(bienBanHopId);
//
//            OutputStream os = response.getOutputStream();
//            document.write(os);
//            os.close();
//        } catch (IOException e) {
//            throw new RuntimeException("Error generating file", e);
//        }
//    }
//
///*//    @GetMapping("/bangbaogia")
////    public void downloadBangBaoGia(HttpServletResponse response, int baoGiaId) {
////        try {
////            XWPFTemplate document = baoGiaTemplateService.generateBaoGiaWordXWPFTemplate(baoGiaId);
////            response.reset();
////            response.setContentType("application/octet-stream");
////            response.setHeader("Content-disposition",
////                    "attachment;filename=user_word_" + System.currentTimeMillis() + ".docx");
////
////            OutputStream os = response.getOutputStream();
////            document.write(os);
////            os.close();
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
////    }*/
//
//
//    @GetMapping("/bangbaogia")
//    public void downloadBangBaoGia(HttpServletResponse response, int baoGiaId) {
//        try {
//            BaoGiaResponse baoGia = baoGiaService.getBaoGiaById(baoGiaId);
//
//            String abbreviation = Arrays.stream(baoGia.getTieuDe().split(" "))
//                    .map(word -> word.substring(0, 1).toUpperCase())
//                    .collect(Collectors.joining());
//
//            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//            String formattedDate = baoGia.getNgayTao().format(outputFormatter);
//
//            String fileName = "BaoGia_" + abbreviation + "_" + formattedDate + ".docx";
//
//            response.reset();
//            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
//            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
//            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
//
//            XWPFTemplate document = baoGiaTemplateService.generateBaoGiaWordXWPFTemplate(baoGiaId);
//
//            OutputStream os = response.getOutputStream();
//            document.write(os);
//            os.close();
//        } catch (IOException e) {
//            throw new RuntimeException("Error generating file", e);
//        }
//    }
//
//}
