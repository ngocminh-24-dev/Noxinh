package org.company.noxinh.repository;

import org.company.noxinh.constants.ProductStatus;
import org.company.noxinh.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * The interface Product repository.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    /**
     * Find by id in page.
     *
     * @param ids      the ids
     * @param pageable the pageable
     * @return the page
     */
    Page<Product> findByIdIn(List<Integer> ids, Pageable pageable);

    @Override
    Page<Product> findAll(Pageable pageable);

    /**
     * Find by id and status product.
     *
     * @param productId     the product id
     * @param productStatus the product status
     * @return the product
     */
    Product findByIdAndStatus(Integer productId, ProductStatus productStatus);

    /**
     * Find by name page.
     *
     * @param productName the product name
     * @param pageable    the pageable
     * @return the page
     */
    @Query(value = "SELECT * FROM Product p " +
            "WHERE (:productName IS NULL " +
            "OR (p.name COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :productName, '%'))) " +
            "AND p.status = 'ACTIVE'", nativeQuery = true)
    Page<Product> findByName(@Param("productName") String productName, Pageable pageable);

    /**
     * Find all by status is not page.
     *
     * @param productStatus the product status
     * @param pageable      the pageable
     * @return the page
     */
    Page<Product> findAllByStatusIsNot(ProductStatus productStatus, Pageable pageable);

    List<Product> findByIdIn(List<Integer> ids);

    List<Product> getByIdIn(Collection<Integer> ids, Sort sort);

    @Query(value = """
SELECT [p].*
FROM [product] p
INNER JOIN [combo_product] [cp] ON [cp].product_id = [p].id
WHERE (:status IS NULL OR [p].status = :status)
AND (:comboId IS NULL OR [cp].combo_id = :comboId)
""",
countQuery = """
SELECT count(p.id)
FROM product p
INNER JOIN combo_product cp ON cp.product_id = p.id
WHERE (:status IS NULL OR p.status = :status)
AND (:comboId IS NULL OR cp.combo_id = :comboId)
""",nativeQuery = true)
    Page<Product> findByStatus(String status, Integer comboId, Pageable pageable);

    @Query(value = """
SELECT DISTINCT [p].*
FROM [product] p
INNER JOIN [combo_product] [cp] ON [cp].product_id = [p].id
WHERE (:status IS NULL OR [p].status = :status)
AND (:comboId IS NULL OR [cp].combo_id = :comboId)
""",
            countQuery = """
SELECT count(p.id)
FROM product p
INNER JOIN combo_product cp ON cp.product_id = p.id
WHERE (:status IS NULL OR p.status = :status)
AND (:comboId IS NULL OR cp.combo_id = :comboId)
""",nativeQuery = true)
    Page<Product> findAll(Pageable pageable, String status, Integer comboId);
}