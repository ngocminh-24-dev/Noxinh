package org.company.noxinh.repository;

import org.company.noxinh.constants.ComboStatus;
import org.company.noxinh.entity.Combo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Combo repository.
 */
@Repository
public interface ComboRepository extends JpaRepository<Combo, Integer> {
    Page<Combo> findAllByIdIsNot(int i, Pageable comboPage);

    Page<Combo> findByIdNotAndStatus(Integer id, ComboStatus status, Pageable pageable);

    Page<Combo> findByIdNot(Integer id, Pageable pageable);
}