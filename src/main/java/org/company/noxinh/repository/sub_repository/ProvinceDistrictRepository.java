package org.company.noxinh.repository.sub_repository;

import org.company.noxinh.entity.sub_entity.ProvinceDistrict;
import org.company.noxinh.entity.sub_entity.id.ProvinceDistrictId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Province district repository.
 */
@Repository
public interface ProvinceDistrictRepository extends JpaRepository<ProvinceDistrict, ProvinceDistrictId> {
    /**
     * Find by province id list.
     *
     * @param provinceId the province id
     * @return the list
     */
    List<ProvinceDistrict> findByProvinceId(Integer provinceId);
}
