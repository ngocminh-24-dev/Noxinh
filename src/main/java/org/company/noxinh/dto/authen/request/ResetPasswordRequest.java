package org.company.noxinh.dto.authen.request;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Reset password request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResetPasswordRequest implements Serializable {
    @NotNull(message = "Email không được để trống")
    @Email(message = "Email phải đúng định dạng")
    private String email;
}
