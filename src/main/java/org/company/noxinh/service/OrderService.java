package org.company.noxinh.service;

import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.order.request.AdminUpdateOrderStatusRequest;
import org.company.noxinh.dto.order.request.CreateOrderRequest;
import org.company.noxinh.dto.order.response.ClientResponse;
import org.company.noxinh.dto.order.response.GetOrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface Order service.
 */
public interface   OrderService {
    /**
     * Gets order by id.
     *
     * @param id the id
     * @return the order by id
     */
    GetOrderResponse getOrderById(Integer id);

    /**
     * Gets order by user id.
     *
     * @param userId the user id
     * @return the order by user id
     */
    List<GetOrderResponse> getOrderByUserId(Integer userId);

    /**
     * Gets list order response by user.
     *
     * @param id the id
     * @return the list order response by user
     */
    ResponseData<List<GetOrderResponse>> getListOrderResponseByUser(Integer id);

    /**
     * Gets order response by id.
     *
     * @param id the id
     * @return the order response by id
     */
    ResponseData<GetOrderResponse> getOrderResponseById(Integer id);

    /**
     * Create order response data.
     *
     * @param request the request
     * @param userId  the user id
     * @return the response data
     */
    ResponseData<String> createOrder(CreateOrderRequest request, Integer userId);

    /**
     * Admin update status response data.
     *
     * @param orderId the order id
     * @param request the request
     * @return the response data
     */
    ResponseData<String> adminUpdateStatus(Integer orderId, AdminUpdateOrderStatusRequest request);

    /**
     * User cancel order response data.
     *
     * @param orderId the order id
     * @param userId  the user id
     * @return the response data
     */
    ResponseData<String> userCancelOrder(Integer orderId, Integer userId);

    ResponseData<Page<GetOrderResponse>> getListOrderResponseByUser(Integer id, Pageable pageable);

    ResponseData<Page<GetOrderResponse>> getListOrders(Pageable pageable, LocalDateTime from, LocalDateTime to);

    ResponseData<Integer> countAllOrders(LocalDateTime from, LocalDateTime to);
    ResponseData<Page<ClientResponse>> getListClients(Pageable pageable, LocalDateTime from, LocalDateTime to, String search);
}
