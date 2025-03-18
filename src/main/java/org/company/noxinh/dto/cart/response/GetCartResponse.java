package org.company.noxinh.dto.cart.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.noxinh.dto.product.ProductResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCartResponse {
    private Integer accountId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductResponseDTO> products;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Page<ProductResponseDTO> page;

    public GetCartResponse(Integer accountId, List<ProductResponseDTO> products) {
        this.accountId = accountId;
        this.products = products;
    }

    public GetCartResponse(Integer accountId, Page<ProductResponseDTO> page) {
        this.accountId = accountId;
        this.page = page;
    }
}
