package org.company.noxinh.entity.sub_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.entity.Combo;
import org.company.noxinh.entity.Product;
import org.company.noxinh.entity.sub_entity.id.ComboProductId;

@Getter
@Setter
@Entity
@Table(name = "combo_product")
@NoArgsConstructor
public class ComboProduct {
    @EmbeddedId
    private ComboProductId id;

    public ComboProduct(Integer comboId, Integer productId) {
        this.id = new ComboProductId(comboId, productId);
    }
}