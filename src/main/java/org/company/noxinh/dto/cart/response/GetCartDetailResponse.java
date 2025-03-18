package org.company.noxinh.dto.cart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.dto.product.ProductResponseDTO;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCartDetailResponse {
    private Integer accountId;
    private ProductResponseDTO product;
}
