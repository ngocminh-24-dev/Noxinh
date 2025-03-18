package org.company.noxinh.dto.authen.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Login response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginResponseDTO implements Serializable {
    private String accessToken;
    private String refreshToken;
}
