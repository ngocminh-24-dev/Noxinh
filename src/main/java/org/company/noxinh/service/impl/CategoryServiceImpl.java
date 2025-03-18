package org.company.noxinh.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.constants.ResponseMessage;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.category.request.CreateCategoryRequestDTO;
import org.company.noxinh.dto.category.request.UpdateCategoryRequestDTO;
import org.company.noxinh.dto.category.response.CategoryResponseDTO;
import org.company.noxinh.entity.Category;
import org.company.noxinh.entity.Product;
import org.company.noxinh.entity.sub_entity.CategoryProduct;
import org.company.noxinh.entity.sub_entity.ComboProduct;
import org.company.noxinh.entity.sub_entity.id.CategoryProductId;
import org.company.noxinh.repository.CategoryRepository;
import org.company.noxinh.repository.ProductRepository;
import org.company.noxinh.repository.sub_repository.CategoryProductRepository;
import org.company.noxinh.service.CategoryService;
import org.company.noxinh.service.ValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Category service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    /**
     * Find by id category.
     *
     * @param id the id
     * @return the category
     */
    public Category findById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public ResponseData<Page<CategoryResponseDTO>> findAll(Pageable pageable) {
        log.info("Start [findAll] processing to find category");
        Page<CategoryResponseDTO> categories = categoryRepository.findCategories(pageable);
        log.info("End [findAll] processing to find category");
        return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S012.getMessage(), categories);
    }


    @Override
    public ResponseData<CategoryResponseDTO> getById(int categoryId) {
        Category category = findById(categoryId);
        if (category == null) {
            return ResponseData.notFound("Không tìm thấy phân loại.", Map.of("errors", "Sản phẩm với id: " + categoryId + " không tìm thấy trong hệ thống."));
        }
        return ResponseData.ok(new CategoryResponseDTO(category), "Lấy thông tin phân loại thành công.");
    }

    @Override
    public Set<Category> findByProductId(int productId) {
        Set<CategoryProduct> comboProducts = categoryProductRepository.findById_ProductId(productId);
        List<Integer> categoryIds = comboProducts.stream().map(CategoryProduct::getId).map(CategoryProductId::getCategoryId).toList();
        if (categoryIds.isEmpty()) {
            return null;
        }
        return categoryRepository.findByIdIn(new HashSet<>(categoryIds));
    }

    @Transactional
    @Override
    public ResponseData<CategoryResponseDTO> deleteById(int categoryId) {
        log.info("Start [deleteById] processing delete category with id: {}", categoryId);
        Category category = findById(categoryId);
        if (category == null) {
            return ResponseData.notFound("Không tìm thấy phân loại.", Map.of("errors", "Loại sản phẩm với id: " + categoryId + " không tìm thấy trong hệ thống."));
        }
        int countProduct = categoryProductRepository.deleteById_CategoryId(categoryId);
        categoryRepository.delete(category);
        log.info("End [deleteById] processing delete category with id: {}", categoryId);
        return ResponseData.ok("Xoá phân loại thành công (" + countProduct + " sản phẩm được phân vào loại này.)");
    }

    public ResponseData<CategoryResponseDTO> createCategory(CreateCategoryRequestDTO request) {
        if (request == null) {
            return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
        }
        log.info("Start [createCategory] processing create category");
        try {
            if (isCategoryExisted(request.getName())) {
                log.info("Category already exists");
                return new ResponseData<>(ResponseCode.C004.getCode(), ResponseMessage.E018.getMessage());
            }
            Category resultOfAdd = categoryRepository.save(new Category(request.getName()));
            CategoryResponseDTO response = modelMapper.map(resultOfAdd, CategoryResponseDTO.class);
            return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S011.getMessage(), response);
        } catch (Exception ex) {
            log.error("Error while creating category: {}", ex.getMessage(), ex);
            return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E019.getMessage());
        } finally {
            log.info("End [createCategory] processing");
        }
    }

    private boolean isCategoryExisted(String categoryName) {
        return categoryRepository.findByName(categoryName).isPresent();
    }

    @Override
    public ResponseData<String> updateCategory(Integer categoryId, UpdateCategoryRequestDTO request) {
        try {
            log.info("Start [updateCategory] processing update category with id: {}", categoryId);
            if (categoryId == null || request == null) {
                return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
            }
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category == null) {
                return new ResponseData<>(ResponseCode.C003.getCode(), ResponseMessage.E020.getMessage());
            }
            ValidationService.updateIfChanged(request.getName(), category.getName(), category::setName);
            if (isCategoryExisted(request.getName())) {
                return new ResponseData<>(ResponseCode.C004.getCode(), ResponseMessage.E018.getMessage());
            }
            category.setName(request.getName());
            categoryRepository.save(category);
            return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S013.getMessage());
        } catch (Exception ex) {
            log.error("Error while update category: {}", ex.getMessage());
        } finally {
            log.info("End [updateCategory] processing update category with id: {}", categoryId);
        }
        return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E021.getMessage());
    }

    @Override
    public ResponseData<String> attachProductToCategory(Integer categoryId, Integer productId) {
        try {
            if (categoryId == null || productId == null) {
                return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
            }
            log.info("Start [attachProductToCategory] processing attach product to category");
            Category category = categoryRepository.findById(categoryId).orElse(null);
            Product product = productRepository.findById(productId).orElse(null);
            if (category == null) {
                return new ResponseData<>(ResponseCode.C003.getCode(), ResponseMessage.E020.getMessage());
            } else if (product == null) {
                return new ResponseData<>(ResponseCode.C003.getCode(), ResponseMessage.E022.getMessage());
            }
            CategoryProduct repository = categoryProductRepository.findByIdCategoryIdAndIdProductId(categoryId, productId);
            if (repository != null) {
                log.info("Product ID {} is existed in category ID {}", productId, categoryId);
                return new ResponseData<>(ResponseCode.C004.getCode(), ResponseMessage.E023.getMessage());
            }
            categoryProductRepository.save(new CategoryProduct(new CategoryProductId(categoryId, productId), category, product));
            return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S014.getMessage());
        } catch (Exception ex) {
            log.error("Error while update attachProductToCategory: {}", ex.getMessage());
        } finally {
            log.info("End [attachProductToCategory] processing attach product to category");
        }
        return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E024.getMessage());
    }

    public Set<Category> findByProductIds(List<Integer> productIds){
        return categoryRepository.findByIdIn(productIds.stream().collect(Collectors.toSet()));
    }
}
