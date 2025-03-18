package org.company.noxinh.dto.product.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.utils.enum_validator.EnumPrice;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductRequest {
    @NotNull(message = "Tên sản phẩm không được để trống")
    private String name;
    @NotNull(message = "Giá sản phẩm không được để trống")
    @EnumPrice(message = "Giá sản phẩm không được nhỏ hơn 0.")
    private Double price;
    @NotNull(message = "Ảnh minh hoạ không được để trống")
    private String image;
    @NotNull(message = "Miêu tả sản phẩm không được để trống")
    private String description;
    @NotNull(message = "Mã sản phẩm không được để trống")
    private String code;
    private Boolean addToSlider;
    private String sliderImage;
    private Integer comboId;

    public void validate() {
        this.name = this.name.trim();
        this.image = this.image.trim();
        this.description = this.description.trim();
        this.code = this.code.trim();
        if (sliderImage != null)
            this.sliderImage = this.sliderImage.trim();
        else
            this.sliderImage = null;
    }
}
