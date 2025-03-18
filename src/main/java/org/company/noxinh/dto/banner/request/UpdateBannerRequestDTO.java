package org.company.noxinh.dto.banner.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Update banner request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateBannerRequestDTO implements Serializable {
    private String title;

    @NotNull(message = "Ảnh banner không được để trống")
    private String image;
}
