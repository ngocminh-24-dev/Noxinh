package org.company.noxinh.dto.banner.request;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Display banner request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DisplayBannerRequestDTO implements Serializable {
    @Size(min = 1, message = "Tối thiểu chọn 1 banner")
    @Size(max = 3, message = "Tối đa chọn 3 banner")
    @NotNull(message = "Danh sách banner không được trống")
    private List<Integer> bannerIds;
}
