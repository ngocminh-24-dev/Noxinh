package org.company.noxinh.repository;

import org.company.noxinh.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * The interface District repository.
 */
@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<District> findByIdIn(Collection<Integer> ids);
}
