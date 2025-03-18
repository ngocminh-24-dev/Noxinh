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
public class LoginAdminRequestDTO implements Serializable {
    @NotEmpty(message = "Phone không thể để trống")
    private String phone;

    @NotEmpty(message = "Mật khẩu không thể để trống !")
    private String password;
}
