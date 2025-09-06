package com.training.patterns.proxy;

import com.training.constant.Channel;
import com.training.dto.OrderHistoryDto;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface OrderHistoryFinder {
    Uni<OrderHistoryDto> findByOrderId(Long orderId, Channel channel);
    Uni<List<OrderHistoryDto>> findByUserId(Long id, Channel channel);
}
