package org.company.noxinh.dto.order.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.constants.OrderStatus;

@Setter
@Getter
@NoArgsConstructor
public class AdminUpdateOrderStatusRequest {
    private OrderStatus orderStatus;
}
