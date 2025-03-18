package org.company.noxinh.dto.authen.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/*
 * The type Confirm password request dto.
 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConfirmPasswordRequestDTO implements Serializable {
    @NotNull(message = "OTP không được trống")
    @Size(min = 6, max = 6, message = "OTP có 6 số")
    private String otp;

    @NotNull(message = "Mật khẩu mới không được bỏ trống")
    private String newPassword;

    @NotNull(message = "Nhập lại mật khẩu mới không được bỏ trống")
    private String confirmPassword;
}
