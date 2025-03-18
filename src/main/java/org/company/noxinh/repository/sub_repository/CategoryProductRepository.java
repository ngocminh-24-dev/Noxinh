package org.company.noxinh.repository.sub_repository;

import org.company.noxinh.entity.sub_entity.CategoryProduct;
import org.company.noxinh.entity.sub_entity.id.CategoryProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * The interface Category product repository.
 */
@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProduct, CategoryProductId> {
    /**
     * Find by id product id set.
     *
     * @param productId the product id
     * @return the set
     */
    Set<CategoryProduct> findById_ProductId(int productId);

    /**
     * Delete by id category id int.
     *
     * @param categoryId the category id
     * @return the int
     */
    int deleteById_CategoryId(int categoryId);

    /**
     * Find by id product id and category id boolean.
     *
     * @param productId  the product id
     * @param categoryId the category id
     * @return the boolean
     */
    CategoryProduct findByIdCategoryIdAndIdProductId(Integer productId, Integer categoryId);

    int deleteById_ProductId(int productId);

    List<CategoryProduct> findById_ProductIdIn(List<Integer> list);
}