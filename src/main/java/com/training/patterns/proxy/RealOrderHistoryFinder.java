package com.training.patterns.proxy;

import com.training.constant.Channel;
import com.training.dto.OrderHistoryDto;
import com.training.exceptions.OrdersNotFoundException;
import com.training.repository.OrderHistoryRepository;
import com.training.util.ObjectMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@jakarta.inject.Named("realFinder")
public class RealOrderHistoryFinder implements OrderHistoryFinder {

    private final OrderHistoryRepository repository;

    public RealOrderHistoryFinder(OrderHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Uni<OrderHistoryDto> findByOrderId(Long orderId, Channel channel) {
        return repository.findByOrderId(orderId)
                .onItem().ifNull().failWith(
                        () -> new OrdersNotFoundException("No se encontró el historial con order ID: " + orderId))
                .onItem().ifNotNull()
                .transform(ObjectMapper::toOrderHistoryDto); // devuelve FULL DTO
    }

    @Override
    public Uni<List<OrderHistoryDto>> findByUserId(Long id, Channel channel) {
        return repository.findByUserId(id)
                .onItem()
                .transformToUni(r -> Uni.createFrom().item(r.stream()
                        .map(ObjectMapper::toOrderHistoryDto)
                        .collect(Collectors.toList())));
    }
}
