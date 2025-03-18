package org.company.noxinh.dto.cart.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateCartRequest {
    private Integer productId;
    private Integer amount;
}
