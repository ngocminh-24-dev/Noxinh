package org.company.noxinh.controller;


import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.company.noxinh.dto.order.response.GetOrderResponse;
import org.company.noxinh.dto.product.ProductResponseDTO;
import org.company.noxinh.entity.Product;
import org.company.noxinh.repository.ProductRepository;
import org.company.noxinh.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
    private final ProductRepository productRepository;
    private final OrderService orderService;

    @GetMapping("/text-file")
    public ResponseEntity<byte[]> downloadTextFile() {
        String content = "This is the content of the text file."; // Your text content
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "file.txt");

        return new ResponseEntity<>(contentBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/products-excel")
    public ResponseEntity<byte[]> downloadProductExcel() throws IOException {
        List<Product> products = productRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Name", "Price", "Image", "Description", "Code", "Slider Image", "Status"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Fill Data Rows
        int rowNum = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getPrice().doubleValue());
            row.createCell(3).setCellValue(product.getImage());
            row.createCell(4).setCellValue(product.getDescription());
            row.createCell(5).setCellValue(product.getCode());
            row.createCell(6).setCellValue(product.getSliderImage());
            row.createCell(7).setCellValue(product.getStatus().getName());
        }

        // Auto-size Columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write Workbook to Byte Array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Set HTTP Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "products.xlsx");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }


}
