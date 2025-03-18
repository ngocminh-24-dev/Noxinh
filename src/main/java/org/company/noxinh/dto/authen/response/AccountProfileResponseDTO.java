package org.company.noxinh.dto.authen.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.company.noxinh.entity.District;
import org.company.noxinh.entity.Province;
import org.company.noxinh.entity.Ward;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountProfileResponseDTO implements Serializable {
    private Integer id;
    private String name;
    private String phone;
    private String specificAddress;
    private District district;
    private Province province;
    private Ward ward;
}
