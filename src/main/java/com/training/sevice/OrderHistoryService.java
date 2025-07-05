package com.training.sevice;

import com.training.dto.OrderHistoryDto;
import com.training.model.OrderHistory;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface OrderHistoryService {

    Uni<OrderHistory> saveOrderHistory(OrderHistory orderHistory);

    Uni<OrderHistoryDto> findByOrderId(Long orderId);
}
