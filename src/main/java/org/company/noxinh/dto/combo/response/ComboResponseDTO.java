package org.company.noxinh.dto.combo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.dto.product.ProductResponseDTO;
import org.company.noxinh.entity.Combo;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ComboResponseDTO {
    private Integer id;
    private String name;
    private String color;
    private String image;
    private String description;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Page<ProductResponseDTO> productPages;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer productCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductResponseDTO> products;

    public ComboResponseDTO(Combo combo, List<ProductResponseDTO> products) {
        this.id = combo.getId();
        this.name = combo.getName();
        this.productCount = products.size();
        this.products = products;
        this.color = combo.getColor();
    }

    public ComboResponseDTO(Combo combo, Page<ProductResponseDTO> productPages) {
        this.id = combo.getId();
        this.name = combo.getName();
        this.productPages = productPages;
        this.color = combo.getColor();
        this.image = combo.getImage();
        this.description = combo.getDescription();
        this.status = combo.getStatus().getName();
    }
}
