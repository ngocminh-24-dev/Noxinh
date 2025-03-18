package org.company.noxinh.dto.admin.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Account resposnse dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountResposnseDTO implements Serializable {
    private Integer id;
    private String name;
    private String phone;
    private String specificAddress;
    private String district;
    private String province;
    private String ward;
}
