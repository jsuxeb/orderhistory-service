package com.training.sevice.impl;

import com.training.model.OrderHistory;
import com.training.repository.OrderHistoryRepository;
import com.training.sevice.OrderHistoryService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderHistoryServiceImpl implements OrderHistoryService {

    @Inject
    OrderHistoryRepository repository;

    @Override
    public Uni<OrderHistory> saveOrderHistory(OrderHistory orderHistory) {
        return repository.save(orderHistory);
    }
}
