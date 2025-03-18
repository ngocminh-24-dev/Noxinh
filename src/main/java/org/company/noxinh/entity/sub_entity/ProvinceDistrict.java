package org.company.noxinh.entity.sub_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.company.noxinh.entity.District;
import org.company.noxinh.entity.Province;
import org.company.noxinh.entity.sub_entity.id.ProvinceDistrictId;

@Getter
@Setter
@Entity
@Table(name = "province_district")
public class ProvinceDistrict {
    @EmbeddedId
    private ProvinceDistrictId id;

    @MapsId("provinceId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    @MapsId("districtId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

}