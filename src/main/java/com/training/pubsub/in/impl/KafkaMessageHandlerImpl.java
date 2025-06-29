package com.training.pubsub.in.impl;

import avro.model.OrderHistory;
import com.training.pubsub.in.KafkaMessageHandler;
import com.training.sevice.OrderHistoryService;
import com.training.util.ObjectMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class KafkaMessageHandlerImpl implements KafkaMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageHandlerImpl.class);
    @Inject
    OrderHistoryService orderHistoryService;

    @Incoming("order-history")
    @Override
    public CompletionStage<Void> handleMessage(Message<OrderHistory> orderEvent) {

        com.training.model.OrderHistory orderHistory = ObjectMapper.toOrderHistory(orderEvent.getPayload());
        log.info("Incoming message from order-history with orderId={} ", orderEvent.getPayload().getId());
        return Uni.createFrom().deferred(() -> orderHistoryService.saveOrderHistory(orderHistory)
                .flatMap(r -> {
                    orderEvent.ack();
                    return Uni.createFrom().voidItem();
                })).subscribeAsCompletionStage();
    }
}
