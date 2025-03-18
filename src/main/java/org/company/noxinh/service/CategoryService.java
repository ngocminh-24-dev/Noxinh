package org.company.noxinh.service;

import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.category.request.UpdateCategoryRequestDTO;
import org.company.noxinh.dto.category.response.CategoryResponseDTO;
import org.company.noxinh.dto.category.request.CreateCategoryRequestDTO;
import org.company.noxinh.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * The interface Category service.
 */
public interface CategoryService {
    /**
     * Gets by id.
     *
     * @param categoryId the category id
     * @return the by id
     */
    ResponseData<CategoryResponseDTO> getById(int categoryId);

    /**
     * Find by product id set.
     *
     * @param productId the product id
     * @return the set
     */
    Set<Category> findByProductId(int productId);

    /**
     * Delete by id response data.
     *
     * @param categoryId the category id
     * @return the response data
     */
    ResponseData<CategoryResponseDTO> deleteById(int categoryId);

    /**
     * Create category response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<CategoryResponseDTO> createCategory(CreateCategoryRequestDTO request);


    /**
     * Find all response data.
     *
     * @param pageable the pageable
     * @return the response data
     */
    ResponseData<Page<CategoryResponseDTO>> findAll(Pageable pageable);

    /**
     * Update category response data.
     *
     * @param categoryId the category id
     * @param request    the request
     * @return the response data
     */
    ResponseData<String> updateCategory(Integer categoryId, UpdateCategoryRequestDTO request);

    /**
     * Attach product to category response data.
     *
     * @param categoryId the category id
     * @param productId  the product id
     * @return the response data
     */
    ResponseData<String> attachProductToCategory(Integer categoryId, Integer productId);

    Set<Category> findByProductIds(List<Integer> productIds);
}
