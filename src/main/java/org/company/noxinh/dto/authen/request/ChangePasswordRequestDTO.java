package org.company.noxinh.dto.authen.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChangePasswordRequestDTO implements Serializable {
    @NotNull(message = "Mật khẩu cũ không được bỏ trống")
    private String currentPassword;

    @NotNull(message = "Mật khẩu mới không được bỏ trống")
    private String newPassword;

    @NotNull(message = "Nhập lại mật khẩu mới không được bỏ trống")
    private String confirmPassword;
}
