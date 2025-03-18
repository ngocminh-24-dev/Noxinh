package org.company.noxinh.service;

import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.cart.request.UpdateCartRequest;
import org.company.noxinh.dto.cart.response.GetCartDetailResponse;
import org.company.noxinh.dto.cart.response.GetCartResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Cart service.
 */
public interface CartService {
    /**
     * Add product to cart response data.
     *
     * @param accountId the account id
     * @param productId the product id
     * @return the response data
     */
    ResponseData<String> addProductToCart(Integer accountId, Integer productId, Integer amount);

    /**
     * Gets product in cart by account id.
     *
     * @param accountId the account id
     * @return the product in cart by account id
     */
    ResponseData<GetCartResponse> getProductInCartByAccountId(Integer accountId);

    /**
     * Remove product from cart response data.
     *
     * @param accountId the account id
     * @param productId the product id
     * @return the response data
     */
    ResponseData<String> removeProductFromCart(Integer accountId, Integer productId);
    ResponseData<GetCartResponse> getProductInCartByAccountId(Integer accountId, Pageable pageable);
    ResponseData<String> updateAmount(UpdateCartRequest request);
    ResponseData<GetCartDetailResponse> getProductInCartByAccountIdAndProductId(Integer accountId, Integer productId);
    Boolean removeProductsFromCart(Integer accountId, List<Integer> productIds) throws Exception;
}
