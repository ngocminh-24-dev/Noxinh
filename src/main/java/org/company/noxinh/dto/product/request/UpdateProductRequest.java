package org.company.noxinh.dto.product.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProductRequest {
    @NotNull(message = "Tên sản phẩm không được để trống")
    private String name;
    @NotNull(message = "Giá không được để trống")
    private BigDecimal price;
    @NotNull(message = "Ảnh minh hoạ không được để trống")
    private String image;
    @NotNull(message = "Miêu tả sản phẩm không được để trống")
    private String description;
    @NotNull(message = "Mã sản phẩm không được để trống")
    private String code;
    private String sliderImage;
}
