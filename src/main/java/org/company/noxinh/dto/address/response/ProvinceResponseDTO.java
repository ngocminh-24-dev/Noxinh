package org.company.noxinh.dto.address.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type District response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProvinceResponseDTO implements Serializable {
    private Integer id;
    private String name;
}
