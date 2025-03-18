package org.company.noxinh.service;

import org.company.noxinh.constants.ProductStatus;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.product.ProductResponseDTO;
import org.company.noxinh.dto.product.request.CreateProductRequest;
import org.company.noxinh.dto.product.request.UpdateProductRequest;
import org.company.noxinh.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface Product service.
 */
public interface ProductService {
    /**
     * Gets product by id.
     *
     * @param productId       the product id
     * @param includeCategory the include category
     * @return the product by id
     */
    ResponseData<ProductResponseDTO> getProductById(int productId, boolean includeCategory);

    /**
     * Gets product by id v 2.
     *
     * @param productId       the product id
     * @param includeCategory the include category
     * @return the product by id v 2
     */
    ProductResponseDTO getProductByIdV2(int productId, boolean includeCategory);

    /**
     * Delete product by id response data.
     *
     * @param productId the product id
     * @return the response data
     */
    ResponseData<ProductResponseDTO> deleteProductById(int productId);

    /**
     * Create product response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<ProductResponseDTO> createProduct(CreateProductRequest request);

    /**
     * Update product status response data.
     *
     * @param id     the id
     * @param status the status
     * @return the response data
     */
    ResponseData<ProductResponseDTO> updateProductStatus(Integer id, ProductStatus status);

    /**
     * Update product response data.
     *
     * @param id      the id
     * @param request the request
     * @return the response data
     */
    ResponseData<ProductResponseDTO> updateProduct(Integer id, UpdateProductRequest request);

    /**
     * Find by id product.
     *
     * @param id the id
     * @return the product
     */
    Product findById(int id);

    /**
     * Find by ids page.
     *
     * @param ids      the ids
     * @param pageable the pageable
     * @return the page
     */
    Page<ProductResponseDTO> findByIds(List<Integer> ids, Pageable pageable);

    /**
     * Find by ids page.
     *
     * @param ids      the ids
     * @return the page
     */
    List<ProductResponseDTO> findByIdsList(List<Integer> ids);

    /**
     * Gets product.
     *
     * @param pageable        the pageable
     * @param includeCategory the include category
     * @return the product
     */
    ResponseData<Page<ProductResponseDTO>> getProduct(Pageable pageable, Boolean includeCategory);

    /**
     * Find by id and status product.
     *
     * @param productId     the product id
     * @param productStatus the product status
     * @return the product
     */
    Product findByIdAndStatus(Integer productId, ProductStatus productStatus);

    /**
     * Gets product by id v 2.
     *
     * @param productId       the product id
     * @param includeCategory the include category
     * @param amount          the amount
     * @param price           the price
     * @return the product by id v 2
     */
    ProductResponseDTO getProductByIdV2(Integer productId, boolean includeCategory, Integer amount, BigDecimal price);

    /**
     * Gets product by name.
     *
     * @param productName the product name
     * @param pageable    the pageable
     * @return the product by name
     */
    ResponseData<Page<ProductResponseDTO>> getProductByName(String productName, Pageable pageable);

    List<Product> findByIds(List<Integer> list);
    List<Product> findByIds(List<Integer> ids, Sort sort);
    List<ProductResponseDTO> getProductByIds(List<Integer> productIds, Boolean includeCategory, Sort sort);
    ResponseData<Integer> countAllOrders();
    ResponseData<Page<ProductResponseDTO>> getProduct(Pageable pageable, Boolean includeCategory, Integer comboId, ProductStatus status);
}
