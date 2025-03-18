package org.company.noxinh.dto.authen.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.company.noxinh.utils.enum_validator.EnumPhone;

import java.io.Serializable;

/**
 * The type Update account response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateAccountResponseDTO implements Serializable {
    private String name;

    @EnumPhone(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;
    private String specificAddress;
    private Integer province_id;
    private Integer district_id;
    private Integer ward_id;
}
