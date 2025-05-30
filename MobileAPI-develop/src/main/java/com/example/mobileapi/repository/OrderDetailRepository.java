package com.example.mobileapi.repository;

import com.example.mobileapi.entity.Order;
import com.example.mobileapi.entity.OrderDetail;
import com.example.mobileapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    List<OrderDetail> findOrderByOrderId(UUID orderId);

    OrderDetail findOrderByProductId(UUID productId);

    @Query("SELECT od.product FROM OrderDetail od WHERE od.order.id = :orderId")
    Product findProductByOrderId(UUID orderId);

    @Query("SELECT od.order FROM OrderDetail od WHERE od.id = :orderDetailId")
    Order findOrderIdByOrderDetailId(UUID orderDetailId);
}
