package com.training.sevice;

import com.training.model.OrderHistory;
import io.smallrye.mutiny.Uni;

public interface OrderHistoryService {

    Uni<OrderHistory> saveOrderHistory(OrderHistory orderHistory);
}
