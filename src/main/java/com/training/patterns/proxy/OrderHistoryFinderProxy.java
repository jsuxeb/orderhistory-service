package com.training.patterns.proxy;

import com.training.constant.Channel;
import com.training.dto.OrderHistoryDto;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class



OrderHistoryFinderProxy implements OrderHistoryFinder {

    private final OrderHistoryFinder real; // delega al real


    public OrderHistoryFinderProxy(@Named("realFinder") OrderHistoryFinder real) {
        this.real = real;

    }
    @Override
    public Uni<OrderHistoryDto> findByOrderId(Long orderId, Channel channel) {
        return real.findByOrderId(orderId, channel)
                .map(dto -> channel == Channel.WEB ? dto : toLightDto(dto));
    }

    @Override
    public Uni<List<OrderHistoryDto>> findByUserId(Long id, Channel channel) {
        return real.findByUserId(id, channel)
                .map(list -> {
                    if (list == null) {
                        return Collections.emptyList();
                    }
                    if (channel == Channel.WEB) {
                        return list;
                    }
                    return list.stream()
                            .map(this::toLightDto)
                            .collect(Collectors.toList());
                });
    }

    private OrderHistoryDto toLightDto(OrderHistoryDto full) {
        OrderHistoryDto light = new OrderHistoryDto();
        light.setOrderId(full.getOrderId());
        light.setCreatedAt(full.getCreatedAt());
        light.setTotalAmount(full.getTotalAmount());
        return light;
    }
}
