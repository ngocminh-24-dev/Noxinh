package org.company.noxinh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.constants.ProductStatus;
import org.company.noxinh.dto.product.request.CreateProductRequest;
import org.company.noxinh.dto.product.request.UpdateProductRequest;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "product")
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;

    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "slider_img", nullable = false, length = 500)
    private String sliderImage;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public Product(CreateProductRequest request) {
        this.name = request.getName();
        this.price = BigDecimal.valueOf(request.getPrice());
        this.image = request.getImage();
        this.description = request.getDescription();
        this.code = request.getCode();
        this.status = ProductStatus.ACTIVE;
        this.sliderImage = request.getSliderImage();
    }

    public Product(UpdateProductRequest request) {
        this.name = request.getName();
        this.price = request.getPrice();
        this.image = request.getImage();
        this.description = request.getDescription();
        this.code = request.getCode();
        this.sliderImage = request.getSliderImage();
        this.status = ProductStatus.INACTIVE;
    }

    public Product(Integer id, String name, BigDecimal price, String image, String description, String code, ProductStatus status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.code = code;
        this.status = status;
    }
}