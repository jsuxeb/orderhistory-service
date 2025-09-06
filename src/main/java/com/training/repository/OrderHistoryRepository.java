package com.training.repository;

import com.training.model.OrderHistory;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface OrderHistoryRepository {

    Uni<OrderHistory> save(OrderHistory orderHistory);

    Uni<OrderHistory> findByOrderId(Long orderId);

    Uni<OrderHistory> update(OrderHistory orderHistory);

    Uni<List<OrderHistory>> findByUserId(Long userId);
}
