package org.company.noxinh.repository;

import org.company.noxinh.entity.Cart;
import org.company.noxinh.entity.sub_entity.id.CartId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The interface Cart repository.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {
    Set<Cart> findById_AccountId(Integer accountId);

    Cart findFirstById_AccountIdAndId_ProductId(Integer accountId, Integer productId);

    Set<Cart> findById_ProductId(Integer productId);

    int deleteById_ProductId(int productId);

    Page<Cart> findById_AccountId(Integer accountId, Pageable pageable);

    List<Cart> findById_AccountIdAndId_ProductIdIn(Integer accountId, Collection<Integer> productIds);
}