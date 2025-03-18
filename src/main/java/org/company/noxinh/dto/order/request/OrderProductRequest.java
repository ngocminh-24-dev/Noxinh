package org.company.noxinh.dto.order.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.utils.enum_validator.EnumInt;

@Getter
@Setter
@NoArgsConstructor
public class OrderProductRequest {
    @NotNull(message = "Mã sản phẩm không được để trống.")
    @EnumInt(min = 1, message = "Mã sản phẩm phải lớn hơn 0.")
    private Integer productId;
    @EnumInt(min = 1, message = "Lượng sản phẩm phải lớn hơn 0.")
    private Integer amount;
}
