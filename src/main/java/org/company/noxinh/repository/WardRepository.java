package org.company.noxinh.repository;

import org.company.noxinh.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Ward repository.
 */
@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
    List<Ward> findByIdIn(List<Integer> id);
}
