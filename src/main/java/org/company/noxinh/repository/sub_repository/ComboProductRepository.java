package org.company.noxinh.repository.sub_repository;

import org.company.noxinh.entity.sub_entity.ComboProduct;
import org.company.noxinh.entity.sub_entity.id.ComboProductId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Combo product repository.
 */
@Repository
public interface ComboProductRepository extends JpaRepository<ComboProduct, ComboProductId> {
    List<ComboProduct> findById_ComboId(int comboId);

    Page<ComboProduct> findById_ComboId(int comboId, Pageable pageable);

    List<ComboProduct> findById_ComboIdIn(List<Integer> comboIds);

    int deleteById_ComboId(int comboId);

    @Query(value = """
SELECT cp.*
FROM [combo_product] cp
INNER JOIN [product] p ON cp.product_id = p.id
WHERE cp.combo_id = :comboId
AND (:priceMin IS NULL OR p.price >= :priceMin)
AND (:priceMax IS NULL OR p.price <= :priceMax)
AND (:search IS NULL OR p.name COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :search, '%'))
""", nativeQuery = true)
    Page<ComboProduct> findById_ComboIdAndPriceMinMax(int comboId, Double priceMin, Double priceMax, Pageable pageable, String search);

    Optional<ComboProduct> findFirstById_ProductId(Integer productId);
}