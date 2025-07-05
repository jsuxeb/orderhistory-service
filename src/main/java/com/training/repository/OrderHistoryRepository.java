package com.training.repository;

import com.training.model.OrderHistory;
import io.smallrye.mutiny.Uni;

public interface OrderHistoryRepository {

    Uni<OrderHistory> save(OrderHistory orderHistory);

    Uni<OrderHistory> findByOrderId(Long orderId);

    Uni<OrderHistory> update(OrderHistory orderHistory);
}
