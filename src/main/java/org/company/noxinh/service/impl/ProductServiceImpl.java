package org.company.noxinh.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.noxinh.constants.ProductStatus;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.category.response.CategoryResponseDTO;
import org.company.noxinh.dto.product.ProductResponseDTO;
import org.company.noxinh.dto.product.request.CreateProductRequest;
import org.company.noxinh.dto.product.request.UpdateProductRequest;
import org.company.noxinh.entity.Category;
import org.company.noxinh.entity.Combo;
import org.company.noxinh.entity.Product;
import org.company.noxinh.entity.sub_entity.CategoryProduct;
import org.company.noxinh.entity.sub_entity.ComboProduct;
import org.company.noxinh.entity.sub_entity.id.CategoryProductId;
import org.company.noxinh.exception.exceptions.NotFoundException;
import org.company.noxinh.repository.CartRepository;
import org.company.noxinh.repository.ComboRepository;
import org.company.noxinh.repository.ProductRepository;
import org.company.noxinh.repository.sub_repository.CategoryProductRepository;
import org.company.noxinh.repository.sub_repository.ComboProductRepository;
import org.company.noxinh.repository.sub_repository.OrderProductRepository;
import org.company.noxinh.service.CategoryService;
import org.company.noxinh.service.ProductService;
import org.company.noxinh.utils.Utilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    public final ProductRepository productRepository;
    public final CategoryService categoryService;
    private final CartRepository cartRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final OrderProductRepository orderProductRepository;
    private final ComboProductRepository comboProductRepository;
    private final ComboRepository comboRepository;

    public Product findById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product getById(int id) throws NotFoundException {
        Product product = findById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return product;
    }

    public List<Product> findByIds(List<Integer> ids) {
        return productRepository.findAllById(ids);
    }

    public List<Product> findByIds(List<Integer> ids, Sort sort) {
        return productRepository.getByIdIn(ids, sort);
    }

    public List<ProductResponseDTO> getProductByIds(List<Integer> ids) {
        List<Product> products = findByIds(ids);
        return products.stream().map(ProductResponseDTO::new).toList();
    }

    public ProductResponseDTO getProductByIdV2(int productId, boolean includeCategory) {
        Product product = findById(productId);
        ComboProduct comboProduct = comboProductRepository.findFirstById_ProductId(productId).orElse(null);
        Combo combo = comboProduct != null ? comboRepository.findById(comboProduct.getId().getComboId()).orElse(null) : null;
        if (product == null) {
            throw new NotFoundException("Sản phẩm với id: " + productId + " không tìm thấy trong hệ thống.");
        }

        if (includeCategory) {
            Set<Category> categories = categoryService.findByProductId(productId);
            if (!categories.isEmpty()) {
                return new ProductResponseDTO(product, categories, combo);
            }
        }
        return new ProductResponseDTO(product, combo);
    }

    @Override
    public ResponseData<ProductResponseDTO> getProductById(int productId, boolean includeCategory) {
        Product product = findById(productId);
        if (product == null) {
            return ResponseData.notFound("Không tìm thấy sản phẩm.", Map.of("errors", "Sản phẩm với id: " + productId + " không tìm thấy trong hệ thống."));
        }

        ComboProduct comboProduct = comboProductRepository.findFirstById_ProductId(productId).orElse(null);
        Combo combo = comboProduct != null ? comboRepository.findById(comboProduct.getId().getComboId()).orElse(null) : null;

        if (includeCategory) {
            Set<Category> categories = categoryService.findByProductId(productId);
            if (categories != null && !categories.isEmpty())
                return ResponseData.ok(new ProductResponseDTO(product, categories, combo), "Lấy thông tin sản phẩm thành công.");
            else
                return ResponseData.ok(new ProductResponseDTO(product, combo), "Lấy thông tin sản phẩm thành công.");
        }
        return ResponseData.ok(new ProductResponseDTO(product, combo), "Lấy thông tin sản phẩm thành công.");
    }

    @Override
    public ResponseData<ProductResponseDTO> deleteProductById(int productId) {
        Product product = findById(productId);
        if (product == null) {
            return ResponseData.notFound("Không tìm thấy sản phẩm.", Map.of("errors", "Sản phẩm với id: " + productId + " không tìm thấy trong hệ thống."));
        }
        productRepository.deleteById(productId);
        cartRepository.deleteById_ProductId(productId);
        categoryProductRepository.deleteById_ProductId(productId);
        orderProductRepository.deleteById_ProductId(productId);
        return ResponseData.ok(null, "Xoá sản phẩm thành công.");
    }

    public ResponseData<ProductResponseDTO> createProduct(CreateProductRequest request) {
        request.validate();
        Product product = new Product(request);

        Product savedProduct = productRepository.save(product);

        if (Boolean.TRUE.equals(request.getAddToSlider())) {
            comboProductRepository.save(new ComboProduct(10, savedProduct.getId()));
        }

        if (request.getComboId() != null && request.getComboId() != 10) {
            Combo combo = comboRepository.findById(request.getComboId()).orElse(null);
            if (combo == null) {
                return ResponseData.notFound("Không tìm thấy combo", Map.of("error", "Không tìm thấy combo với id: " + request.getComboId() + "."));
            }

            comboProductRepository.save(new ComboProduct(combo.getId(), savedProduct.getId()));
        }
        return ResponseData.ok(new ProductResponseDTO(savedProduct), "Tạo sản phẩm mới thành công.");
    }

    public ResponseData<ProductResponseDTO> updateProduct(Integer id, UpdateProductRequest request) {
        Product oldProduct = getById(id);
        Product newProduct = new Product(request);

        oldProduct = updateProduct(newProduct, oldProduct);

        productRepository.save(oldProduct);
        return ResponseData.ok(new ProductResponseDTO(oldProduct), "Tạo sản phẩm mới thành công.");
    }

    public Product updateProduct(Product newProduct, Product oldProduct) {
        oldProduct.setName(Utilities.processString(newProduct.getName(), oldProduct.getName()));
        oldProduct.setDescription(Utilities.processString(newProduct.getDescription(), oldProduct.getDescription()));
        oldProduct.setImage(Utilities.processString(newProduct.getImage(), oldProduct.getImage()));
        oldProduct.setPrice(Utilities.processBigDecimal(newProduct.getPrice(), oldProduct.getPrice()));
        oldProduct.setCode(Utilities.processString(newProduct.getCode(), oldProduct.getCode()));
        oldProduct.setSliderImage(Utilities.processString(newProduct.getSliderImage(), oldProduct.getSliderImage()));
        return oldProduct;
    }

    public ResponseData<ProductResponseDTO> updateProductStatus(Integer id, ProductStatus status) {
        Product product = getById(id);
        product.setStatus(status);
        productRepository.save(product);
        return ResponseData.ok(new ProductResponseDTO(product), "Cập nhập trạng thái sản phẩm thành công.");
    }

    @Override
    public ResponseData<Page<ProductResponseDTO>> getProduct(Pageable pageable, Boolean includeCategory) {
        Page<Product> products = productRepository.findAllByStatusIsNot(ProductStatus.INACTIVE, pageable);
        List<Integer> productIds = products.get().map(Product::getId).distinct().toList();
        if (includeCategory) {
            List<CategoryProduct> categoryProducts = categoryProductRepository.findById_ProductIdIn(productIds);
            Set<CategoryResponseDTO> categories = categoryService.findByProductIds(productIds).stream().map(CategoryResponseDTO::new).collect(Collectors.toSet());
            List<ProductResponseDTO> result = new ArrayList<>();
            for (Product product : products.getContent()) {
                List<Integer> cateIds = categoryProducts.stream().filter(cp -> cp.getId().getProductId().equals(product.getId())).map(CategoryProduct::getId).map(CategoryProductId::getCategoryId).toList();
                Set<CategoryResponseDTO> cates = categories.stream().filter(ct -> cateIds.contains(ct.getId())).collect(Collectors.toSet());
                ComboProduct comboProduct = comboProductRepository.findFirstById_ProductId(product.getId()).orElse(null);
                Combo combo = comboProduct != null ? comboRepository.findById(comboProduct.getId().getComboId()).orElse(null) : null ;
                if (!cates.isEmpty())
                    result.add(new ProductResponseDTO(product, cates, true, combo));
                else
                    result.add(new ProductResponseDTO(product, null, true, combo));
            }
            return ResponseData.ok(new PageImpl<>(result, pageable, products.getContent().size()), "Tìm sản phẩm thành công.");
        }
        return ResponseData.ok(new PageImpl<ProductResponseDTO>(products.get().map(ProductResponseDTO::new).toList(), pageable, products.getTotalElements()), "Tìm sản phẩm thành công.");
    }

    @Override
    public ResponseData<Page<ProductResponseDTO>> getProduct(Pageable pageable, Boolean includeCategory, Integer comboId, ProductStatus status) {
        Page<Product> products = productRepository.findAll(pageable, status == null ? null : status.name(), comboId);
//        Page<Product> products = productRepository.findByStatus(status == null ? null : status.name(), categoryId, pageable);

        List<Integer> productIds = products.get().map(Product::getId).distinct().toList();
        if (includeCategory) {
            List<CategoryProduct> categoryProducts = categoryProductRepository.findById_ProductIdIn(productIds);
            Set<CategoryResponseDTO> categories = categoryService.findByProductIds(productIds).stream().map(CategoryResponseDTO::new).collect(Collectors.toSet());
            List<ProductResponseDTO> result = new ArrayList<>();
            for (Product product : products.getContent()) {
                List<Integer> cateIds = categoryProducts.stream().filter(cp -> cp.getId().getProductId().equals(product.getId())).map(CategoryProduct::getId).map(CategoryProductId::getCategoryId).toList();
                Set<CategoryResponseDTO> cates = categories.stream().filter(ct -> cateIds.contains(ct.getId())).collect(Collectors.toSet());
                ComboProduct comboProduct = comboProductRepository.findFirstById_ProductId(product.getId()).orElse(null);
                Combo combo = comboProduct != null ? comboRepository.findById(comboProduct.getId().getComboId()).orElse(null) : null ;
                if (!cates.isEmpty())
                    result.add(new ProductResponseDTO(product, cates, true, combo));
                else
                    result.add(new ProductResponseDTO(product, null, true, combo));
            }
            return ResponseData.ok(new PageImpl<>(result, pageable, products.getTotalElements()), "Tìm sản phẩm thành công.");
        }
        return ResponseData.ok(new PageImpl<>(products.get().map(ProductResponseDTO::new).toList(), pageable, products.getTotalElements()), "Tìm sản phẩm thành công.");
    }

    @Override
    public List<ProductResponseDTO> getProductByIds(List<Integer> productIds, Boolean includeCategory, Sort sort) {
        List<Product> products = productRepository.getByIdIn(productIds, sort);
        List<ProductResponseDTO> result = new ArrayList<>();
        if (includeCategory) {
            List<CategoryProduct> categoryProducts = categoryProductRepository.findById_ProductIdIn(productIds);
            Set<CategoryResponseDTO> categories = categoryService.findByProductIds(productIds).stream().map(CategoryResponseDTO::new).collect(Collectors.toSet());
            for (Product product : products) {
                List<Integer> cateIds = categoryProducts.stream().filter(cp -> cp.getId().getProductId().equals(product.getId())).map(CategoryProduct::getId).map(CategoryProductId::getCategoryId).toList();
                Set<CategoryResponseDTO> cates = categories.stream().filter(ct -> cateIds.contains(ct.getId())).collect(Collectors.toSet());
                ComboProduct comboProduct = comboProductRepository.findFirstById_ProductId(product.getId()).orElse(null);
                Combo combo = comboProduct != null ? comboRepository.findById(comboProduct.getId().getComboId()).orElse(null) : null ;
                if (!cates.isEmpty())
                    result.add(new ProductResponseDTO(product, cates, true, combo));
                else
                    result.add(new ProductResponseDTO(product, null, true, combo));
            }
            return result;
        }
        return result;
    }

    @Override
    public Product findByIdAndStatus(Integer productId, ProductStatus productStatus) {
        return productRepository.findByIdAndStatus(productId, productStatus);
    }

    @Override
    public ProductResponseDTO getProductByIdV2(Integer productId, boolean includeCategory, Integer amount, BigDecimal price) {
        Product product = findById(productId);
        if (product == null) {
            throw new NotFoundException("Sản phẩm với id: " + productId + " không tìm thấy trong hệ thống.");
        }
        if (price != null)
            product.setPrice(price);

        if (includeCategory) {
            Set<Category> categories = categoryService.findByProductId(productId);
            if (categories != null && !categories.isEmpty()) {
                return new ProductResponseDTO(product, categories, amount);
            }
        }
        return new ProductResponseDTO(product, amount);
    }

    @Override
    public Page<ProductResponseDTO> findByIds(List<Integer> ids, Pageable pageable) {
        Page<Product> products = productRepository.findByIdIn(ids, pageable);
        return new PageImpl<>(products.get().map(ProductResponseDTO::new).toList(), pageable, products.getContent().size());
    }

    @Override
    public List<ProductResponseDTO> findByIdsList(List<Integer> ids) {
        return productRepository.findByIdIn(ids).stream().map(ProductResponseDTO::new).toList();

    }

    @Override
    public ResponseData<Page<ProductResponseDTO>> getProductByName(String productName, Pageable pageable) {
        Page<Product> products = productRepository.findByName(productName.trim(), pageable);
        List<ProductResponseDTO> productResponseDTOS = new ArrayList<>();
        for (Product product : products) {
            ComboProduct comboProduct = comboProductRepository.findFirstById_ProductId(product.getId()).orElse(null);
            Combo combo = comboProduct != null ? comboRepository.findById(comboProduct.getId().getComboId()).orElse(null) : null ;
            productResponseDTOS.add(new ProductResponseDTO(product, null , true, combo));
        }
        Page<ProductResponseDTO> response = new PageImpl<>(productResponseDTOS, pageable, products.getTotalElements());
        return ResponseData.ok(response, "Lấy sản phẩm thành công.");
    }

    @Override
    public ResponseData<Integer> countAllOrders(){
        return ResponseData.ok(Integer.parseInt(String.valueOf(productRepository.count())), "Đếm tổng số đơn hàng thành công.");
    }
}
