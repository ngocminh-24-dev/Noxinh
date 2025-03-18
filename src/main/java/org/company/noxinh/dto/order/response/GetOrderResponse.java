package org.company.noxinh.dto.order.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.company.noxinh.dto.address.response.DistrictResponseDTO;
import org.company.noxinh.dto.address.response.ProvinceResponseDTO;
import org.company.noxinh.dto.address.response.WardResponseDTO;
import org.company.noxinh.dto.product.ProductResponseDTO;
import org.company.noxinh.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GetOrderResponse {
    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String recipientName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String recipientPhone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProvinceResponseDTO province;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DistrictResponseDTO district;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private WardResponseDTO ward;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal shippingFee;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String specificAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalPrice;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime completeTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductResponseDTO> products;

    public GetOrderResponse(Order order, ProvinceResponseDTO province, DistrictResponseDTO district, WardResponseDTO ward, List<ProductResponseDTO> products) {
        this.id = order.getId();
        this.recipientName = order.getRecipientName();
        this.recipientPhone = order.getRecipientPhone();
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.shippingFee = order.getShippingFee();
        this.specificAddress = order.getSpecificAddress();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus().getName();
        this.createTime = order.getCreateTime();
        this.completeTime = order.getCompleteTime();
        this.products = products;
    }

    public GetOrderResponse(Order order, List<ProductResponseDTO> products) {
        this.id = order.getId();
        this.recipientName = order.getRecipientName();
        this.recipientPhone = order.getRecipientPhone();
        this.shippingFee = order.getShippingFee();
        this.specificAddress = order.getSpecificAddress();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus().getName();
        this.createTime = order.getCreateTime();
        this.completeTime = order.getCompleteTime();
        this.products = products;
    }

    public GetOrderResponse(Order order) {
        this.id = order.getId();
        this.recipientName = order.getRecipientName();
        this.recipientPhone = order.getRecipientPhone();
        this.shippingFee = order.getShippingFee();
        this.specificAddress = order.getSpecificAddress();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus().getName();
        this.createTime = order.getCreateTime();
        this.completeTime = order.getCompleteTime();
    }
}
