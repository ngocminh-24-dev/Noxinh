package org.company.noxinh.repository;

import org.company.noxinh.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Province repository.
 */
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    List<Province> findByIdIn(List<Integer> id);
}
