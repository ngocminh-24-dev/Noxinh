package org.company.noxinh.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.company.noxinh.dto.category.response.CategoryResponseDTO;
import org.company.noxinh.entity.Category;
import org.company.noxinh.entity.Combo;
import org.company.noxinh.entity.Product;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProductResponseDTO {
    private Integer id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer amount;
    private String name;
    private String description;
    private Double price;
    private String image;
    private String code;
    private Boolean status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String comboName;
    private String sliderImage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<CategoryResponseDTO> categories;

    public ProductResponseDTO(Product product, Set<Category> categories, Combo combo) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice().doubleValue();
        this.image = product.getImage();
        this.status = product.getStatus().getBooleanStatus();
        this.code = product.getCode();
        this.comboName = combo.getName();
        this.categories = categories.stream().map(CategoryResponseDTO::new).collect(Collectors.toSet());
        this.sliderImage = product.getSliderImage();
    }

    public ProductResponseDTO(Product product, Set<Category> categories, Integer amount) {
        this.id = product.getId();
        this.amount = amount;
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice().doubleValue();
        this.image = product.getImage();
        this.status = product.getStatus().getBooleanStatus();
        this.code = product.getCode();
        this.categories = categories.stream().map(CategoryResponseDTO::new).collect(Collectors.toSet());
        this.sliderImage = product.getSliderImage();
    }

    public ProductResponseDTO(Product product, Set<CategoryResponseDTO> categories, boolean a, Combo combo) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice().doubleValue();
        this.image = product.getImage();
        this.status = product.getStatus().getBooleanStatus();
        this.code = product.getCode();
        this.categories = categories;
        this.sliderImage = product.getSliderImage();
        this.comboName = combo != null ? combo.getName() : null;
    }

    public ProductResponseDTO(Product product, Combo combo) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice().doubleValue();
        this.image = product.getImage();
        this.code = product.getCode();
        this.comboName = combo != null ? combo.getName() : null;
        this.status = product.getStatus().getBooleanStatus();
        this.sliderImage = product.getSliderImage();
    }

    public ProductResponseDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice().doubleValue();
        this.image = product.getImage();
        this.code = product.getCode();
        this.status = product.getStatus().getBooleanStatus();
        this.sliderImage = product.getSliderImage();
    }

    public ProductResponseDTO(Product product, Integer amount) {
        this.id = product.getId();
        this.amount = amount;
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice().doubleValue();
        this.image = product.getImage();
        this.code = product.getCode();
        this.status = product.getStatus().getBooleanStatus();
        this.sliderImage = product.getSliderImage();
    }
}
