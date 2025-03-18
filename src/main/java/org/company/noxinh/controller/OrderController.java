package org.company.noxinh.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.order.request.AdminUpdateOrderStatusRequest;
import org.company.noxinh.dto.order.request.CreateOrderRequest;
import org.company.noxinh.dto.order.response.GetOrderResponse;
import org.company.noxinh.dto.product.ProductResponseDTO;
import org.company.noxinh.service.OrderService;
import org.company.noxinh.utils.ServiceUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The type Order controller.
 */
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/list-clients")
    public ResponseEntity<ResponseData> getClientsList(Pageable pageable,
                                                       @RequestParam(required = false) LocalDateTime from,
                                                       @RequestParam(required = false) LocalDateTime to,
                                                       @RequestParam(required = false) String search){
        return ResponseEntity.ok(orderService.getListClients(pageable, from, to, search));
    }

    @GetMapping("/user")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<Page<GetOrderResponse>>> getOrderByAccountId(Pageable pageable) {
        ResponseData<Page<GetOrderResponse>> response = orderService.getListOrderResponseByUser(ServiceUtils.getId(), pageable);
        return getResponse(response);
    }

    @GetMapping("/get-all")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<Page<GetOrderResponse>>> getAllOrder(Pageable pageable, @RequestParam(required = false) LocalDateTime from, @RequestParam(required = false) LocalDateTime to) {
        ResponseData<Page<GetOrderResponse>> response = orderService.getListOrders(pageable, from, to);
        return getResponse(response);
    }

    /**
     * Gets order by order id.
     *
     * @param orderId the order id
     * @return the order by order id
     */
    @GetMapping("/{orderId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<GetOrderResponse>> getOrderByOrderId(@PathVariable Integer orderId) {
        ResponseData<GetOrderResponse> response = orderService.getOrderResponseById(orderId);
        return getResponse(response);
    }

    /**
     * Create order response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/")
    public ResponseEntity<ResponseData<String>> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        Integer userId = null;
        try {
            userId  = ServiceUtils.getId();
        } catch (Exception e){

        }
        ResponseData<String> response = orderService.createOrder(request, userId);
        return getResponse(response);
    }

    /**
     * Admin update order status response entity.
     *
     * @param orderId the order id
     * @param request the request
     * @return the response entity
     */
    @PutMapping("/admin-update/{orderId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<String>> adminUpdateOrderStatus(@PathVariable Integer orderId, AdminUpdateOrderStatusRequest request) {
        ResponseData<String> response = orderService.adminUpdateStatus(orderId, request);
        return getResponse(response);
    }

    /**
     * User update order status response entity.
     *
     * @param orderId the order id
     * @param request the request
     * @return the response entity
     */
    @PutMapping("/user-update/{orderId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<String>> userUpdateOrderStatus(@PathVariable Integer orderId, AdminUpdateOrderStatusRequest request) {
        Integer userId = ServiceUtils.getId();
        ResponseData<String> response = orderService.userCancelOrder(orderId, userId);
        return getResponse(response);
    }

    @GetMapping("/count-all")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<Integer>> countAll(@RequestParam(required = false) LocalDateTime from, @RequestParam(required = false) LocalDateTime to) {
        return getResponse(orderService.countAllOrders(from, to));
    }

    /**
     * Gets response.
     *
     * @param <T>      the type parameter
     * @param response the response
     * @return the response
     */
    public <T> ResponseEntity<ResponseData<T>> getResponse(ResponseData<T> response) {
        if (response.getStatus() == ResponseCode.C001.getCode())
            return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }

    @GetMapping("/download-excel")
    public ResponseEntity<byte[]> downloadOrderExcel(Pageable pageable, @RequestParam(required = false) LocalDateTime from, @RequestParam(required = false) LocalDateTime to) throws IOException {
        List<GetOrderResponse> orders = orderService.getListOrders(pageable, from, to).getData().getContent(); // Fetch orders

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Orders");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Create a cell style with borders
        CellStyle borderedStyle = workbook.createCellStyle();
        borderedStyle.setBorderTop(BorderStyle.THIN);
        borderedStyle.setBorderBottom(BorderStyle.THIN);
        borderedStyle.setBorderLeft(BorderStyle.THIN);
        borderedStyle.setBorderRight(BorderStyle.THIN);

        // Create a header style with bold font and borders
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Create center-aligned style for merged cells
        CellStyle centerAlignedStyle = workbook.createCellStyle();
        centerAlignedStyle.setAlignment(HorizontalAlignment.CENTER);
        centerAlignedStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        centerAlignedStyle.setBorderTop(BorderStyle.THIN);
        centerAlignedStyle.setBorderBottom(BorderStyle.THIN);
        centerAlignedStyle.setBorderLeft(BorderStyle.THIN);
        centerAlignedStyle.setBorderRight(BorderStyle.THIN);

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Mã Đơn Hàng", "Tên Người Nhận", "Số Điện Thoại Người Nhận", "Địa Chỉ Cụ Thể", "Tỉnh/Thành Phố", "Quận/Huyện", "Phường/Xã",
                "Phí Vận Chuyển", "Tổng Giá", "Trạng Thái", "Thời Gian Tạo",
                "Thời Gian Hoàn Thành", "Mã Sản Phẩm", "Số Lượng Sản Phẩm", "Tên Sản Phẩm"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle); // Apply header style
        }

        // Fill Data Rows
        int rowNum = 1;
        for (GetOrderResponse order : orders) {
            int startRow = rowNum; // Starting row of the order section
            boolean firstProduct = true; // Flag to check if it’s the first product row for the order
            for (ProductResponseDTO product : order.getProducts()) {
                Row row = sheet.createRow(rowNum++);

                if (firstProduct) {
                    // Fill order details only once
                    createCellWithStyle(row, 0, order.getId(), borderedStyle);
                    createCellWithStyle(row, 1, order.getRecipientName(), borderedStyle);
                    createCellWithStyle(row, 2, order.getRecipientPhone(), borderedStyle);
                    createCellWithStyle(row, 3, order.getSpecificAddress(), borderedStyle);
                    createCellWithStyle(row, 4, order.getProvince() != null ? order.getProvince().getName() : "", borderedStyle);
                    createCellWithStyle(row, 5, order.getDistrict() != null ? order.getDistrict().getName() : "", borderedStyle);
                    createCellWithStyle(row, 6, order.getWard() != null ? order.getWard().getName() : "", borderedStyle);
                    createCellWithStyle(row, 7, order.getShippingFee() != null ? order.getShippingFee().doubleValue() : 0, borderedStyle);
                    createCellWithStyle(row, 8, order.getTotalPrice() != null ? order.getTotalPrice().doubleValue() : 0, borderedStyle);
                    createCellWithStyle(row, 9, order.getStatus(), borderedStyle);
                    createCellWithStyle(row, 10, order.getCreateTime() != null ? order.getCreateTime().format(formatter) : "", borderedStyle);
                    createCellWithStyle(row, 11, order.getCompleteTime() != null ? order.getCompleteTime().format(formatter) : "", borderedStyle);

                    firstProduct = false; // Set to false after filling the order details
                }

                // Fill product details in each row
                createCellWithStyle(row, 12, product.getId(), borderedStyle);
                createCellWithStyle(row, 13, product.getAmount(), borderedStyle);
                createCellWithStyle(row, 14, product.getName(), borderedStyle);
            }

            // Merge cells for empty order details and apply borders to merged region
            if (order.getProducts().size() > 1) {
                for (int col = 0; col <= 11; col++) {
                    CellRangeAddress region = new CellRangeAddress(startRow, rowNum - 1, col, col);
                    sheet.addMergedRegion(region);

                    // Apply the centered style to each merged cell
                    Row startOrderRow = sheet.getRow(startRow);
                    Cell cell = startOrderRow.getCell(col);
                    cell.setCellStyle(centerAlignedStyle);

                    // Apply border to each cell in the merged region
                    RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
                }
            }
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
        headers.setContentDispositionFormData("attachment", "orders.xlsx");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

    }
    private void createCellWithStyle(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }
        cell.setCellStyle(style); // Apply style with borders
    }
}
