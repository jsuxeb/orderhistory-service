package com.training.repository;

import com.training.model.OrderHistory;
import io.smallrye.mutiny.Uni;

public interface OrderHistoryRepository {

    Uni<OrderHistory> save(OrderHistory orderHistory);
}
