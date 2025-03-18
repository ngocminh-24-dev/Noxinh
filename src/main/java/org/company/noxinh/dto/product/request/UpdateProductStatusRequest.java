package org.company.noxinh.dto.product.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.constants.ProductStatus;

@Setter
@Getter
@NoArgsConstructor
public class UpdateProductStatusRequest {
    private ProductStatus productStatus;
}
