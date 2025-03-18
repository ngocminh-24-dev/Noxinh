package org.company.noxinh.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.noxinh.constants.ComboStatus;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.combo.request.CreateComboRequest;
import org.company.noxinh.dto.combo.request.UpdateComboRequest;
import org.company.noxinh.dto.combo.response.ComboResponseDTO;
import org.company.noxinh.dto.product.ProductResponseDTO;
import org.company.noxinh.entity.Combo;
import org.company.noxinh.entity.sub_entity.ComboProduct;
import org.company.noxinh.entity.sub_entity.id.ComboProductId;
import org.company.noxinh.repository.ComboRepository;
import org.company.noxinh.repository.sub_repository.ComboProductRepository;
import org.company.noxinh.service.ComboService;
import org.company.noxinh.service.ProductService;
import org.company.noxinh.utils.Utilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Combo service.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ComboServiceImpl implements ComboService {
    private final ComboRepository comboRepository;
    private final ComboProductRepository comboProductRepository;
    private final ProductService productService;

    /**
     * Find by id combo.
     *
     * @param id the id
     * @return the combo
     */
    public Combo findById(int id) {
        return comboRepository.findById(id).orElse(null);
    }

    /**
     * Find combo product by combo id list.
     *
     * @param comboId the combo id
     * @return the list
     */
    public List<ComboProduct> findComboProductByComboId(int comboId) {
        return comboProductRepository.findById_ComboId(comboId);
    }

    public Page<ComboProduct> findComboProductByComboId(int comboId, Pageable pageable) {
        return comboProductRepository.findById_ComboId(comboId, pageable);
    }

    /**
     * Get combo by id combo response dto.
     *
     * @param id the id
     * @return the combo response dto
     */
    public ComboResponseDTO getComboById(int id, Pageable pageable) {
        Combo combo = findById(id);

        if (combo == null) return null;

        Page<ComboProduct> comboProducts = findComboProductByComboId(id, pageable);
        List<Integer> productIds = comboProducts.getContent().stream().map(ComboProduct::getId).map(ComboProductId::getProductId).toList();
        List<ProductResponseDTO> products = productService.findByIdsList(productIds);

        return new ComboResponseDTO(combo, new PageImpl<>(products, pageable, comboProducts.getTotalElements()));
    }

    public ResponseData<ComboResponseDTO> getComboResponseById(int id, Pageable pageable) {
        ComboResponseDTO response = getComboById(id, pageable);
        if (response == null)
            return ResponseData.failed("Không tìm thấy combo.", Map.of("error", "Không tìm thấy combo với id: " + id + "."));
        return ResponseData.ok(response, "Lấy thông tin combo thành công.");
    }

    public ResponseData<String> addProductToCombo(int comboId, int productId) {
        findById(comboId);
        productService.findById(productId);
        Optional<ComboProduct> comboProduct = comboProductRepository.findById(new ComboProductId(comboId, productId));
        if (comboProduct.isPresent())
            return ResponseData.failed("Combo đã chứa sản phẩm này.", Map.of("error", "Combo với id: " + comboId + " đã chứa sản phẩm với id: " + productId + "."));

        comboProductRepository.save(new ComboProduct(comboId, productId));
        return ResponseData.ok("Thêm sản phẩm vào combo thành công.");
    }

    public ResponseData<String> removeProductFromCombo(int comboId, int productId) {
        Combo combo = findById(comboId);
        if (combo == null)
            return ResponseData.failed("Không tìm thấy combo.", Map.of("error", "Không tìm thấy combo với id: " + comboId + "."));
        Optional<ComboProduct> comboProduct = comboProductRepository.findById(new ComboProductId(comboId, productId));
        if (comboProduct.isEmpty())
            return ResponseData.failed("Không tìm thấy combo chứa sản phẩm này.", Map.of("error", "Không tìm thấy combo với id: " + comboId + " và sản phẩm với id: " + productId + "."));

        comboProductRepository.delete(comboProduct.get());
        return ResponseData.ok("Xoá sản phẩm khỏi combo thành công.");
    }

    @Transactional
    public ResponseData<String> deleteCombo(int comboId) {
        Combo combo = findById(comboId);
        if (combo == null)
            return ResponseData.failed("Không tìm thấy combo.", Map.of("error", "Không tìm thấy combo với id: " + comboId + "."));
        comboProductRepository.deleteById_ComboId(comboId);
        comboRepository.delete(combo);
        return ResponseData.ok("Xoá combo thành công.");
    }

    public ResponseData<String> createCombo(CreateComboRequest request) {
        Combo combo = new Combo(request);
        comboRepository.save(combo);
        return ResponseData.ok("Tạo combo thành công.");
    }

    @Override
    public ResponseData<Page<ComboResponseDTO>> getCombo(Pageable comboPage, Pageable productPage) {
        Page<Combo> combos = comboRepository.findByIdNot(10, comboPage);
        List<Integer> comboIds = combos.get().map(Combo::getId).toList();
        List<ComboProduct> comboProducts = comboProductRepository.findById_ComboIdIn(comboIds);
        List<ComboProduct> limitedComboProducts = comboProducts.stream()
                .collect(Collectors.groupingBy(cp -> cp.getId().getComboId()))
                .values().stream()
                .flatMap(list -> list.stream().limit(productPage.getPageSize() * comboIds.size()))
                .toList();
        Sort productSort = productPage.getSort();
        List<ProductResponseDTO> products = productService.getProductByIds(limitedComboProducts.stream().map(ComboProduct::getId).map(ComboProductId::getProductId).distinct().toList(), true, productSort).stream().toList();
        List<ComboResponseDTO> comboResponses = new ArrayList<>();
        for (Combo combo : combos) {
            List<ComboProduct> comboProductsForThis = comboProducts.stream().filter(cp -> cp.getId().getComboId().equals(combo.getId())).toList();
            List<Integer> productIds = comboProductsForThis.stream().map(ComboProduct::getId).map(ComboProductId::getProductId).toList();
            List<ProductResponseDTO> productResponseDTOS = products.stream().filter(p -> productIds.contains(p.getId())).limit(productPage.getPageSize()).toList();
            comboResponses.add(new ComboResponseDTO(combo, new PageImpl<>(productResponseDTOS, productPage, productResponseDTOS.size())));
        }

        return ResponseData.ok(new PageImpl<>(comboResponses, comboPage, comboResponses.size()),"Lấy combo thành công.");
    }

    @Override
    public ResponseData<String> adminUpdateComboStatus(Integer comboId, ComboStatus status) {
        Combo combo = findById(comboId);
        if (combo == null)
            return ResponseData.failed("Không tìm thấy combo.", Map.of("error", "Không tìm thấy combo với id: " + comboId + "."));
        combo.setStatus(status);
        comboRepository.save(combo);
        return ResponseData.ok("Cập nhập trạng thái combo thành công.");
    }

    public ResponseData<String> updateCombo(UpdateComboRequest request, Integer comboId) {
        Combo combo = findById(comboId);
        if (combo == null)
            return ResponseData.failed("Không tìm thấy combo.", Map.of("error", "Không tìm thấy combo với id: " + comboId + "."));
        combo.setDescription(Utilities.processString(request.getDescription(), combo.getDescription()));
        combo.setName(Utilities.processString(request.getName(), combo.getName()));
        combo.setImage(Utilities.processString(request.getImage(), combo.getImage()));
        combo.setColor(Utilities.processString(request.getColor(), combo.getColor()));
        comboRepository.save(combo);
        return ResponseData.ok("Cập nhập thông tin combo thành công.");
    }


    public Page<ComboProduct> findComboProductByComboIdWithMinPriceAndMaxPrice(int comboId, Pageable pageable, Double minPrice, Double maxPrice, String search) {
        return comboProductRepository.findById_ComboIdAndPriceMinMax(comboId, minPrice, maxPrice, pageable, search);
    }

    public ComboResponseDTO getComboById(int id, Pageable pageable, Double minPrice, Double maxPrice, String search) {
        Combo combo = findById(id);

        if (combo == null) return null;

        Page<ComboProduct> comboProducts = findComboProductByComboIdWithMinPriceAndMaxPrice(id, pageable, minPrice, maxPrice, search);
        List<Integer> productIds = comboProducts.getContent().stream().map(ComboProduct::getId).map(ComboProductId::getProductId).toList();
        List<ProductResponseDTO> products = productService.getProductByIds(productIds, true, pageable.getSort());

        return new ComboResponseDTO(combo, new PageImpl<>(products, pageable, comboProducts.getTotalElements()));
    }

    public ResponseData<ComboResponseDTO> getComboResponseById(int id, Pageable pageable, Double minPrice, Double maxPrice, String search) {
        ComboResponseDTO response = getComboById(id, pageable, minPrice, maxPrice, search);
        if (response == null)
            return ResponseData.failed("Không tìm thấy combo.", Map.of("error", "Không tìm thấy combo với id: " + id + "."));
        return ResponseData.ok(response, "Lấy thông tin combo thành công.");
    }
}
