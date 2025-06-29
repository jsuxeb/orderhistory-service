package com.training.repository.impl;

import com.mongodb.client.model.Filters;
import com.training.model.OrderHistory;
import com.training.repository.OrderHistoryRepository;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.conversions.Bson;

@ApplicationScoped
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository {

    @Inject
    ReactiveMongoClient reactiveMongoClient;

    private ReactiveMongoCollection<OrderHistory> getCollection() {
        return reactiveMongoClient.getDatabase("orderdb")
                .getCollection("orderhistory", OrderHistory.class);
    }


    @Override
    public Uni<OrderHistory> save(OrderHistory orderHistory) {
        return getCollection().insertOne(orderHistory)
                .onItem()
                .ifNotNull()
                .transform(o -> orderHistory);
    }

    @Override
    public Uni<OrderHistory> findByOrderId(Long orderId) {
        Bson filter = Filters.eq("orderhistory._id", orderId);
        return getCollection()
                .findOneAndDelete(filter)
                .onFailure()
                .retry().atMost(3);
    }


}
