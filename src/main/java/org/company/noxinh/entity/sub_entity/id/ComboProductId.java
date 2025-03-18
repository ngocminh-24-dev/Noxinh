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
public class ComboProductId implements Serializable {
    private Integer comboId;
    private Integer productId;
}
