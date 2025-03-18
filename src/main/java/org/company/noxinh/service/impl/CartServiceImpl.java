package org.company.noxinh.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.noxinh.constants.ProductStatus;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.cart.request.UpdateCartRequest;
import org.company.noxinh.dto.cart.response.GetCartDetailResponse;
import org.company.noxinh.dto.cart.response.GetCartResponse;
import org.company.noxinh.dto.product.ProductResponseDTO;
import org.company.noxinh.entity.Account;
import org.company.noxinh.entity.Cart;
import org.company.noxinh.entity.Product;
import org.company.noxinh.entity.sub_entity.id.CartId;
import org.company.noxinh.repository.CartRepository;
import org.company.noxinh.service.AccountService;
import org.company.noxinh.service.CartService;
import org.company.noxinh.utils.ServiceUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductServiceImpl productServiceImpl;
    private final AccountService accountService;

    public Set<Cart> findByAccountId(Integer accountId) {
        return cartRepository.findById_AccountId(accountId);
    }

    public Set<Cart> findByProductId(Integer productId) {
        return cartRepository.findById_ProductId(productId);
    }

    public boolean isProductInclude(Integer accountId, Integer productId) {
        return cartRepository.findFirstById_AccountIdAndId_ProductId(accountId, productId) != null;
    }

    public GetCartResponse findProductInCartByAccountId(Integer accountId) {
        Set<Cart> carts = findByAccountId(accountId);
        List<Integer> productIds = carts.stream().map(cart -> cart.getId().getProductId()).toList();
        List<ProductResponseDTO> productResponseDTOS = productServiceImpl.getProductByIds(productIds);
        return new GetCartResponse(accountId, mappingResponse(carts.stream().toList(), productResponseDTOS));
    }

    public ResponseData<GetCartResponse> getProductInCartByAccountId(Integer accountId) {
        Account account = accountService.findById(accountId);
        if (account == null) {
            return ResponseData.notFound("Không tìm thấy tài khoản.", Map.of("error", "Không tìm thấy tài khoản với id: " + accountId + "."));
        }
        return ResponseData.ok(findProductInCartByAccountId(accountId), "Lấy thông tin sản phẩm trong giỏ hàng thành công.");
    }

    @Override
    public ResponseData<String> addProductToCart(Integer accountId, Integer productId, Integer amount) {
        if (isProductInclude(accountId, productId)) {
            return ResponseData.failed("Sản phẩm đã có trong giỏ hàng.", Map.of("error", "Sản phẩm đã có trong giỏ hàng."));
        }
        try {
            if (accountId == null || productId == null) {
                return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
            }
            log.info("Start [addProductToCart] processing attach product to cart");
            Product product = productServiceImpl.findById(productId);
            if (product == null) {
                return ResponseData.notFound("Sản phẩm không tìm thấy.", Map.of("reason", "Sản phẩm với id : " + productId + " không tìm thấy."));
            }
            if (product.getStatus().equals(ProductStatus.INACTIVE)) {
                return ResponseData.failed("Không thể thêm sản phẩm vào giỏ hàng.", Map.of("reason", "Sản phẩm không còn được bán."));
            }
            cartRepository.save(new Cart(new CartId(accountId, productId), amount));
            return ResponseData.ok("Thêm vào giỏ hàng thành công.");
        } catch (Exception ex) {
            log.error("Error while update addProductToCart: {}", ex.getMessage());
            return ResponseData.internalServer("Có lỗi khi thêm sản phẩm vào giỏ hàng.", Map.of("error", ex.getMessage()));
        } finally {
            log.info("End [addProductToCart] processing attach product to cart");
        }
    }

    @Override
    public ResponseData<String> removeProductFromCart(Integer accountId, Integer productId) {
        if (!isProductInclude(accountId, productId)) {
            return ResponseData.failed("Sản phẩm không có trong giỏ hàng.", Map.of("reason", "Sản phẩm không có trong giỏ hàng."));
        }
        try {
            if (accountId == null || productId == null) {
                return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
            }
            log.info("Start [removeProductFromCart] processing remove product from cart");
            Product product = productServiceImpl.findById(productId);
            if (product == null) {
                return ResponseData.notFound("Sản phẩm không tìm thấy.", Map.of("reason", "Sản phẩm với id : " + productId + " không tìm thấy."));
            }
            Cart cart = cartRepository.findFirstById_AccountIdAndId_ProductId(accountId, productId);
            if (cart == null) {
                return ResponseData.notFound("Sản phẩm không nằm trong giỏ hàng.", Map.of("reason", "Sản phẩm với id : " + productId + " không nằm trong giỏ hàng."));
            }
            cartRepository.delete(cart);
            return ResponseData.ok("Xoá khỏi giỏ hàng thành công.");
        } catch (Exception ex) {
            log.error("Error while remove product from cart: {}", ex.getMessage());
            return ResponseData.internalServer("Có lỗi khi thêm sản phẩm vào giỏ hàng.", Map.of("error", ex.getMessage()));
        } finally {
            log.info("End [removeProductFromCart] processing attach product to category");
        }
    }

    @Override
    public Boolean removeProductsFromCart(Integer accountId, List<Integer> productIds) throws Exception {
        try {
            List<Cart> carts = cartRepository.findById_AccountIdAndId_ProductIdIn(accountId, productIds);
            cartRepository.deleteAll(carts);
            return true;
        } catch (Exception ex) {
            log.error("Error while remove product from cart: {}", ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            log.info("End [removeProductsFromCart].");
        }
    }

    public GetCartResponse findProductInCartByAccountId(Integer accountId, Pageable pageable) {
        Page<Cart> carts = findByAccountId(accountId, pageable);
        List<Integer> productIds = carts.getContent().stream().map(cart -> cart.getId().getProductId()).toList();
        List<ProductResponseDTO> productResponseDTOS = productServiceImpl.getProductByIds(productIds);
        return new GetCartResponse(accountId, new PageImpl<>(mappingResponse(carts.getContent(), productResponseDTOS), pageable, productIds.size()));
    }

    public GetCartDetailResponse findProductDetails(Integer accountId, Integer productId) {
        Cart cart = cartRepository.findFirstById_AccountIdAndId_ProductId(accountId, productId);
        if (cart == null) {
            return null;
        }
        ProductResponseDTO productResponseDTOS = productServiceImpl.getProductByIdV2(productId, true, cart.getAmount(), null);
        return new GetCartDetailResponse(accountId, productResponseDTOS);
    }

    public List<ProductResponseDTO> mappingResponse(List<Cart> carts, List<ProductResponseDTO> productResponseDTOS) {
        for (Cart cart : carts) {
            ProductResponseDTO productResponseDTO = productResponseDTOS.stream().filter(pd -> pd.getId().equals(cart.getId().getProductId())).findFirst().orElse(null);
            if (productResponseDTO != null)
                productResponseDTO.setAmount(cart.getAmount());
        }
        return productResponseDTOS;
    }

    public Page<Cart> findByAccountId(Integer accountId, Pageable pageable) {
        return cartRepository.findById_AccountId(accountId, pageable);
    }

    @Override
    public ResponseData<GetCartResponse> getProductInCartByAccountId(Integer accountId, Pageable pageable) {
        Account account = accountService.findById(accountId);
        if (account == null) {
            return ResponseData.notFound("Không tìm thấy tài khoản.", Map.of("error", "Không tìm thấy tài khoản với id: " + accountId + "."));
        }
        return ResponseData.ok(findProductInCartByAccountId(accountId, pageable), "Lấy thông tin sản phẩm trong giỏ hàng thành công.");
    }

    @Override
    public ResponseData<GetCartDetailResponse> getProductInCartByAccountIdAndProductId(Integer accountId, Integer productId) {
        Account account = accountService.findById(accountId);
        if (account == null) {
            return ResponseData.notFound("Không tìm thấy tài khoản.", Map.of("error", "Không tìm thấy tài khoản với id: " + accountId + "."));
        }
        GetCartDetailResponse result = findProductDetails(accountId, productId);
        if (result == null)
            return ResponseData.notFound("Không tồn tại sản phẩm trong giỏ hàng");
        return ResponseData.ok(result, "Lấy thông tin sản phẩm trong giỏ hàng thành công.");
    }

    @Override
    public ResponseData<String> updateAmount(UpdateCartRequest request){
        try {
            Cart cart = cartRepository.findFirstById_AccountIdAndId_ProductId(ServiceUtils.getId(), request.getProductId());
            if (cart == null) {
                return ResponseData.notFound("Không tìm thấy sản phẩm.", Map.of("error", "Không tìm thấy sản phẩm với id: " + request.getProductId() + "."));
            }
            cart.setAmount(request.getAmount());
            cartRepository.save(cart);
        } catch (Exception ex) {
            return ResponseData.internalServer("Có lỗi khi cập nhập số lượng", Map.of("error", ex.getMessage()));
        }
        return ResponseData.ok("Cập nhập số lượng thành công");
    }
}
