package com.training.pubsub.in;

import avro.model.OrderHistory;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

public interface KafkaMessageHandler {

    CompletionStage<Void> handleMessage(Message<OrderHistory> orderEvent);
}
