package org.company.noxinh.dto.authen.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.company.noxinh.utils.enum_validator.EnumPhone;

import java.io.Serializable;

/**
 * The type Login request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDTO implements Serializable {
    @NotEmpty(message = "Phone không thể để trống")
    @EnumPhone(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;

    @NotEmpty(message = "Mật khẩu không thể để trống !")
    private String password;
}
