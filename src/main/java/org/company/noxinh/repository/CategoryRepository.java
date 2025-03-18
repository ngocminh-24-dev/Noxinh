package org.company.noxinh.repository;

import org.company.noxinh.dto.category.response.CategoryResponseDTO;
import org.company.noxinh.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * The interface Category repository.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    /**
     * Find by id in set.
     *
     * @param categoryIds the category ids
     * @return the set
     */
    Set<Category> findByIdIn(Set<Integer> categoryIds);

    /**
     * Find by name optional.
     *
     * @param categoryName the category name
     * @return the optional
     */
    Optional<Category> findByName(String categoryName);

    /**
     * Find categories page.
     *
     * @param pageable the pageable
     * @return the page
     */
    @Query(value = "SELECT new org.company.noxinh.dto.category.response.CategoryResponseDTO(c.id, c.name)" +
            "FROM Category c")
    Page<CategoryResponseDTO> findCategories(Pageable pageable);
}