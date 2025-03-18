package org.company.noxinh.dto.order.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.entity.District;
import org.company.noxinh.entity.Order;
import org.company.noxinh.entity.Province;
import org.company.noxinh.entity.Ward;

@Getter
@Setter
@NoArgsConstructor
public class ClientResponse {
    private Integer id;
    private String name;
    private String phone;
    private String specificAddress;
    private String district;
    private String province;
    private String ward;

    public ClientResponse(Order order, District district, Province province, Ward ward) {
        this.id = order.getUserId();
        this.name = order.getRecipientName();
        this.phone = order.getRecipientPhone();
        this.specificAddress = order.getSpecificAddress();
        this.district = district.getName();
        this.province = province.getName();
        this.ward = ward.getName();
    }
}
