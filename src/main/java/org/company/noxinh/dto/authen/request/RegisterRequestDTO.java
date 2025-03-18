package org.company.noxinh.dto.authen.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.company.noxinh.utils.enum_validator.EnumPhone;

import java.io.Serializable;

/**
 * The type Register request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequestDTO implements Serializable {
    @NotNull(message = "Name không được bỏ trống")
    private String name;

    @NotEmpty(message = "Phone không thể để trống")
    @EnumPhone(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;

    @NotNull(message = "Password không được bỏ trống")
    private String password;

    @NotNull(message = "Địa chỉ không được bỏ trống")
    private String specificAddress;

    @NotNull(message = "Thành phố không được để trống")
    private Integer province_id;

    @NotNull(message = "Quận không được để trống")
    private Integer district_id;

    @NotNull(message = "Phường không được để trống")
    private Integer ward_id;

    @NotNull(message = "Email không được để trống")
    @Email(message = "Email phải đúng định dạng")
    private String email;
}
