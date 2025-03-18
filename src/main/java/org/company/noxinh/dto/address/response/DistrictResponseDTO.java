package org.company.noxinh.dto.address.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type District response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DistrictResponseDTO implements Serializable {
    private Integer id;
    private String name;
}
