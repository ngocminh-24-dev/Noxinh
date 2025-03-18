package org.company.noxinh.service;

import org.company.noxinh.constants.ComboStatus;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.combo.request.CreateComboRequest;
import org.company.noxinh.dto.combo.request.UpdateComboRequest;
import org.company.noxinh.dto.combo.response.ComboResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComboService {
    ResponseData<ComboResponseDTO> getComboResponseById(int id, Pageable pageable);

    ResponseData<String> addProductToCombo(int comboId, int productId);

    ResponseData<String> removeProductFromCombo(int comboId, int productId);

    ResponseData<String> deleteCombo(int comboId);

    ResponseData<String> updateCombo(UpdateComboRequest request, Integer comboId);

    ResponseData<String> createCombo(CreateComboRequest request);

    ResponseData<Page<ComboResponseDTO>> getCombo(Pageable pageable, Pageable productPage);

    ResponseData<String> adminUpdateComboStatus(Integer comboId, ComboStatus status);

    ResponseData<ComboResponseDTO> getComboResponseById(int id, Pageable pageable, Double minPrice, Double maxPrice, String search);
}
