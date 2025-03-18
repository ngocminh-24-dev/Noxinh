package org.company.noxinh.dto.banner.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * The type Banner response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BannerResponseDTO implements Serializable {
    private String title;
    private String image;
    private Instant createTime;
}
