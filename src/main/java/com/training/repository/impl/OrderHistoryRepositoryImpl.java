package com.training.repository.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.training.model.OrderHistory;
import com.training.repository.OrderHistoryRepository;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.conversions.Bson;

import java.util.List;

@ApplicationScoped
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository {

    private static final Log log = LogFactory.getLog(OrderHistoryRepositoryImpl.class);
    @Inject
    ReactiveMongoClient reactiveMongoClient;

    private ReactiveMongoCollection<OrderHistory> getCollection() {
        return reactiveMongoClient.getDatabase("orderdb")
                .getCollection("orderhistory", OrderHistory.class);
    }


    @Override
    public Uni<OrderHistory> save(OrderHistory orderHistory) {
        log.info("Entro a GUARDAR");
        return getCollection().insertOne(orderHistory)
                .onItem()
                .ifNotNull()
                .transform(o -> orderHistory);
    }

    @Override
    public Uni<OrderHistory> update(OrderHistory orderHistory) {
        log.info("Entro a actualizar");
        Bson filter = Filters.eq("_id", orderHistory.getId());
        Bson update = Updates.combine(
                Updates.set("status", orderHistory.getStatus())

        );


        return getCollection().updateOne(filter, update)
                .onItem()
                .ifNotNull()
                .transform(o -> orderHistory);
    }

    @Override
    public Uni<OrderHistory> findByOrderId(Long orderId) {
        Bson filter = Filters.eq("_id", orderId);
        return getCollection()
                .find(filter)
                .toUni();
    }
    public Uni<List<OrderHistory>> findByUserId(Long userId){
        Bson filter = Filters.eq("userId", userId);
        return getCollection()
                .find(filter)
                .collect().asList();
    }


}
