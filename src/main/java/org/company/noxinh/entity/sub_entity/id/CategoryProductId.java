package org.company.noxinh.entity.sub_entity.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CategoryProductId implements Serializable {
    private Integer categoryId;
    private Integer productId;
}
