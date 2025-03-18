package org.company.noxinh.entity.sub_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.company.noxinh.entity.District;
import org.company.noxinh.entity.Ward;
import org.company.noxinh.entity.sub_entity.id.DistrictWardId;

@Getter
@Setter
@Entity
@Table(name = "district_ward")
public class DistrictWard {
    @EmbeddedId
    private DistrictWardId id;

    @MapsId("districtId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @MapsId("wardId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;
}