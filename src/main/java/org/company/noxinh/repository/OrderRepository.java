package org.company.noxinh.repository;

import org.company.noxinh.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface Order repository.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Integer userId);

    Page<Order> findByUserId(Integer userId, Pageable pageable);

    @Query(
            value = """
        SELECT *
        FROM [order] o
        WHERE (:from IS NULL OR (o.create_time >= :from))
        AND (:to IS NULL OR (o.create_time <= :to))
        """,
            countQuery = """
        SELECT COUNT(1)
        FROM [order] o
        WHERE (:from IS NULL OR (o.create_time >= :from))
        AND (:to IS NULL OR (o.create_time <= :to))
        """,
            nativeQuery = true
    )
    Page<Order> getListOrders(LocalDateTime from, LocalDateTime to, Pageable pageable);

    @Query(
            value = """
        SELECT count(o.id)
        FROM [order] o
        WHERE (:from IS NULL OR (o.create_time >= :from))
        AND (:to IS NULL OR (o.create_time <= :to))
        """, nativeQuery = true
    )
    Integer count(LocalDateTime from, LocalDateTime to);

    @Query(
            value = """
        WITH RankedOrders AS (
            SELECT *,
                   ROW_NUMBER() OVER (
                       PARTITION BY recipient_name, recipient_phone, specific_address, province_id, ward_id, district_id
                       ORDER BY create_time DESC
                       ) AS row_num
            FROM [order]
            WHERE (:from IS NULL OR create_time >= :from)
            AND (:to IS NULL OR create_time <= :to)
            AND (:search IS NULL OR (recipient_name COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :search, '%')) OR (recipient_phone COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :search, '%')))
        )
        SELECT *
        FROM RankedOrders
        WHERE row_num = 1
        """, countQuery = """
        WITH RankedOrders AS (
            SELECT *,
                   ROW_NUMBER() OVER (
                       PARTITION BY recipient_name, recipient_phone, specific_address, province_id, ward_id, district_id
                       ORDER BY create_time DESC
                       ) AS row_num
            FROM [order]
            WHERE (:from IS NULL OR create_time >= :from)
            AND (:to IS NULL OR create_time <= :to)
            AND (:search IS NULL OR (recipient_name COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :search, '%')) OR (recipient_phone COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :search, '%')))
        )
        SELECT *
        FROM RankedOrders
        WHERE row_num = 1
""", nativeQuery = true
    )
    Page<Order> getListClients(LocalDateTime from, LocalDateTime to, Pageable pageable, String search);
}