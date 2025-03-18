package org.company.noxinh.entity.sub_entity.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductId implements Serializable {
    private Integer orderId;
    private Integer productId;
}
