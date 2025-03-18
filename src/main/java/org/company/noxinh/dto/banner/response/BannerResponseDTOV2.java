package org.company.noxinh.dto.banner.response;

import java.io.Serializable;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Banner response dtov 2.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BannerResponseDTOV2 implements Serializable {
    private Integer id;
    private String title;
    private String image;
    private Instant createTime;
    private Boolean isDisplay;
}
