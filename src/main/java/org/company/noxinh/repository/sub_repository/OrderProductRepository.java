package org.company.noxinh.repository.sub_repository;

import org.company.noxinh.entity.sub_entity.OrderProduct;
import org.company.noxinh.entity.sub_entity.id.OrderProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Order product repository.
 */
@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductId> {
    int deleteById_ProductId(int productId);

    List<OrderProduct> findById_OrderId(Integer orderId);
}