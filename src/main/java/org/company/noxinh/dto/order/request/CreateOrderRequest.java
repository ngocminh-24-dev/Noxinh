package org.company.noxinh.dto.order.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class    CreateOrderRequest {
    @NotNull(message = "Mã tỉnh không được để trống")
    private Integer provinceId;

    @NotNull(message = "Mã phường không được để trống")
    private Integer districtId;

    @NotNull(message = "Mã đường không được để trống")
    private Integer wardId;

    @NotNull(message = "Địa chỉ cụ thể không được để trống")
    private String specificAddress;

    @JsonIgnore
    private BigDecimal shippingFee;

    @NotNull(message = "Tên người nhận không được để trống")
    private String recipientName;

    @NotNull(message = "Số điện thoại người nhận không được để trống")
    private String recipientPhone;

    @NotNull(message = "Sản phẩm không được để trống")
    private List<OrderProductRequest> products;
}
