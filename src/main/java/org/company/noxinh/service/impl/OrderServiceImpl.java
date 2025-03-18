package org.company.noxinh.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.noxinh.constants.OrderStatus;
import org.company.noxinh.constants.ProductStatus;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.address.response.DistrictResponseDTO;
import org.company.noxinh.dto.address.response.ProvinceResponseDTO;
import org.company.noxinh.dto.address.response.WardResponseDTO;
import org.company.noxinh.dto.order.request.AdminUpdateOrderStatusRequest;
import org.company.noxinh.dto.order.request.CreateOrderRequest;
import org.company.noxinh.dto.order.request.OrderProductRequest;
import org.company.noxinh.dto.order.response.ClientResponse;
import org.company.noxinh.dto.order.response.GetOrderResponse;
import org.company.noxinh.dto.product.ProductResponseDTO;
import org.company.noxinh.entity.*;
import org.company.noxinh.entity.sub_entity.OrderProduct;
import org.company.noxinh.entity.sub_entity.id.OrderProductId;
import org.company.noxinh.exception.exceptions.NotFoundException;
import org.company.noxinh.repository.OrderRepository;
import org.company.noxinh.repository.sub_repository.OrderProductRepository;
import org.company.noxinh.service.AddressService;
import org.company.noxinh.service.CartService;
import org.company.noxinh.service.OrderService;
import org.company.noxinh.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The type Order service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductService productService;
    private final AddressService addressService;
    private final CartService cartService;

    /**
     * Find by id order.
     *
     * @param id the id
     * @return the order
     * @throws NotFoundException the not found exception
     */
    public Order findById(Integer id) throws NotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Đơn hàng không tìm thấy.", Map.of("error", "Không tìm thấy đơn hàng với id: " + id + ".")));
    }

    /**
     * Gets order product by order id.
     *
     * @param orderId the order id
     * @return the order product by order id
     */
    public List<OrderProduct> getOrderProductByOrderId(Integer orderId) {
        return orderProductRepository.findById_OrderId(orderId);
    }

    public GetOrderResponse getOrderById(Integer id) {
        Order order = orderRepository.findById(id).orElse(null);
        List<OrderProduct> orderProducts = getOrderProductByOrderId(id);
        List<ProductResponseDTO> products = new ArrayList<>();
        orderProducts.forEach(orderProduct -> products.add(productService.getProductByIdV2(orderProduct.getId().getProductId(), true, orderProduct.getAmount(), orderProduct.getPrice())));
        WardResponseDTO ward = addressService.getWardById(order.getWardId());
        ProvinceResponseDTO province = addressService.getProvinceById(order.getProvinceId());
        DistrictResponseDTO district = addressService.getDistrictById(order.getDistrictId());

        return new GetOrderResponse(order, province, district, ward, products);
    }

    public ResponseData<GetOrderResponse> getOrderResponseById(Integer id) {
        GetOrderResponse response = getOrderById(id);
        return ResponseData.ok(response, "Lấy thông tin đơn hàng thành công.");
    }

    public List<GetOrderResponse> getOrderByUserId(Integer userId) {
        List<GetOrderResponse> result = new ArrayList<>();
        List<Order> orders = orderRepository.findByUserId(userId);
        for (Order order : orders) {
            List<OrderProduct> orderProducts = getOrderProductByOrderId(order.getId());
            List<Integer> productIds = orderProducts.stream().map(OrderProduct::getId).map(OrderProductId::getProductId).toList();
            List<ProductResponseDTO> products = new ArrayList<>();
            productIds.forEach(productId -> products.add(productService.getProductByIdV2(productId, false)));
            WardResponseDTO ward = addressService.getWardById(order.getWardId());
            ProvinceResponseDTO province = addressService.getProvinceById(order.getProvinceId());
            DistrictResponseDTO district = addressService.getDistrictById(order.getDistrictId());

            result.add(new GetOrderResponse(order, province, district, ward, products));
        }
        return result;
    }

    public ResponseData<List<GetOrderResponse>> getListOrderResponseByUser(Integer id) {
        List<GetOrderResponse> response = getOrderByUserId(id);
        return ResponseData.ok(response, "Lấy thông tin đơn hàng thành công.");
    }

    public ResponseData<Page<ClientResponse>> getListClients(Pageable pageable, LocalDateTime from, LocalDateTime to, String search){
        Page<Order> orders = orderRepository.getListClients(from, to, pageable, search);
        List<Integer> wardIds = orders.getContent().stream().map(Order::getWardId).distinct().toList();
        List<Integer> districtIds = orders.getContent().stream().map(Order::getDistrictId).distinct().toList();
        List<Integer> provinceIds = orders.getContent().stream().map(Order::getProvinceId).distinct().toList();
        List<Ward> wards = addressService.getWardByIds(wardIds);
        List<Province> provinces = addressService.getProvinceByIds(provinceIds);
        List<District> districts = addressService.getDistrictByIds(districtIds);

        List<ClientResponse> clients = orders.getContent().stream().map(order -> new ClientResponse(order,
                districts.stream().filter(w -> Objects.equals(w.getId(), order.getDistrictId())).findFirst().get(),
                provinces.stream().filter(w -> Objects.equals(w.getId(), order.getProvinceId())).findFirst().get(),
                wards.stream().filter(w -> Objects.equals(w.getId(), order.getWardId())).findFirst().get())).toList();
        return ResponseData.ok(new PageImpl<>(clients, pageable, orders.getTotalElements()), "");
    }

    @Transactional
    public ResponseData<String> createOrder(CreateOrderRequest request, Integer userId) {
        if (request.getShippingFee() == null)
            request.setShippingFee(BigDecimal.ZERO);
        Order order = orderRepository.save(new Order(request, userId));
        List<OrderProductRequest> productIds = request.getProducts().stream().toList();
        List<OrderProduct> orderProducts = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        for (OrderProductRequest orderProductRequest : productIds) {
            Product product = productService.findByIdAndStatus(orderProductRequest.getProductId(), ProductStatus.ACTIVE);
            if (product == null)
                return ResponseData.failed("Đơn hàng chứa sản phẩm không không hợp lệ.", Map.of("error", "Sản phẩm với id: " + orderProductRequest + " không còn được bán nữa.", "id", orderProductRequest.toString()));
            products.add(product);
            orderProducts.add(new OrderProduct(new OrderProductId(order.getId(), orderProductRequest.getProductId()), product.getPrice(), orderProductRequest.getAmount()));
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Product product : products) {
            totalPrice = totalPrice.add(product.getPrice());
        }

        try {
            cartService.removeProductsFromCart(userId, products.stream().map(Product::getId).toList());
        } catch (Exception e){
            return ResponseData.internalServer("Xoá sản phẩm trong giỏ hàng thất bại.", Map.of("error", e.getMessage()));
        }

        order.setTotalPrice(totalPrice.add(order.getShippingFee()));
        orderRepository.save(order);
        orderProductRepository.saveAll(orderProducts);
        return ResponseData.ok("Tạo đơn hàng thành công.");
    }

    public ResponseData<String> adminUpdateStatus(Integer orderId, AdminUpdateOrderStatusRequest request) {
        if (!request.getOrderStatus().equals(OrderStatus.COMPLETE) && !request.getOrderStatus().equals(OrderStatus.CANCELED))
            return ResponseData.failed("Trạng thái đơn hàng không phù hợp.");

        Order order = findById(orderId);

        order.setStatus(request.getOrderStatus());
        orderRepository.save(order);
        return ResponseData.ok("Cập nhập trạng thái đơn hàng thành công.");
    }

    public ResponseData<String> userCancelOrder(Integer orderId, Integer userId) {
        Order order = findById(orderId);

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        return ResponseData.ok("Huỷ đơn hàng thành công.");
    }

    Page<GetOrderResponse> getOrderByUserId(Integer userId,Pageable pageable){
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);

        return new PageImpl<>(orders.getContent().stream().map(GetOrderResponse::new).toList(), pageable, orders.getTotalElements());
    }

    @Override
    public ResponseData<Page<GetOrderResponse>> getListOrderResponseByUser(Integer id, Pageable pageable) {
        return ResponseData.ok(getOrderByUserId(id, pageable), "Tạo đơn hàng thành công.");
    }

    @Override
    public ResponseData<Page<GetOrderResponse>> getListOrders(Pageable pageable, LocalDateTime from, LocalDateTime to) {
        Page<Order> orders = orderRepository.getListOrders(from, to, pageable);
        List<GetOrderResponse> result = new ArrayList<>();
        for (Order order : orders.getContent()) {
            List<OrderProduct> orderProducts = getOrderProductByOrderId(order.getId());
            List<Integer> productIds = orderProducts.stream().map(OrderProduct::getId).map(OrderProductId::getProductId).toList();
            List<ProductResponseDTO> products = new ArrayList<>();
            for (Integer productId : productIds) {
                OrderProduct orderProduct = orderProducts.stream().filter(op -> op.getId().getProductId().equals(productId)).findFirst().get();
                products.add(productService.getProductByIdV2(productId, false, orderProduct.getAmount(), null));
            }
            WardResponseDTO ward = addressService.getWardById(order.getWardId());
            ProvinceResponseDTO province = addressService.getProvinceById(order.getProvinceId());
            DistrictResponseDTO district = addressService.getDistrictById(order.getDistrictId());

            result.add(new GetOrderResponse(order, province, district, ward, products));
        }
        return ResponseData.ok(new PageImpl<>(result, pageable, orders.getTotalElements()), "Lấy danh sách đơn hàng thành công.");
    }

    public ResponseData<Integer> countAllOrders(LocalDateTime from, LocalDateTime to){
        return ResponseData.ok(orderRepository.count(from, to), "Đếm tổng số đơn hàng thành công.");
    }
}
