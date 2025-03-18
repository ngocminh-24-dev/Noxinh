package org.company.noxinh.dto.category.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateCategoryRequestDTO implements Serializable {
    @NotNull(message = "Loại sản phẩm không được để trống")
    private String name;
}
