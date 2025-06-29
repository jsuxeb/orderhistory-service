package com.training.util;

import com.training.model.OrderHistory;
import com.training.model.Status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectMapper {
    public static OrderHistory toOrderHistory(avro.model.OrderHistory orderAvro) {
        OrderHistory orderHistory = new OrderHistory();

        orderHistory.setId(orderAvro.getId());
        orderHistory.setUserId(orderAvro.getUserId());
        orderHistory.setOrderDate(orderAvro.getOrderDate() != null ? orderAvro.getOrderDate() : null);

        if (orderAvro.getItems() != null) {
            List<com.training.model.OrderItem> item = orderAvro.getItems().stream()
                    .map(itemAvro -> {
                        com.training.model.OrderItem dtoItem = new com.training.model.OrderItem();
                        dtoItem.setSku(itemAvro.getSku());
                        dtoItem.setProductName(itemAvro.getProductName());
                        dtoItem.setQuantity(itemAvro.getQuantity());
                        dtoItem.setUnitPrice(itemAvro.getUnitPrice());
                        dtoItem.setSubtotal(itemAvro.getSubtotal());
                        return dtoItem;
                    })
                    .collect(Collectors.toList());
            orderHistory.setItems(item);
        }

        Status status = new Status();
        status.setStatus(orderAvro.getStatus());
        status.setStatusDate(orderAvro.getOrderDate() != null ? orderAvro.getOrderDate() : null);
        orderHistory.setTotalAmount(orderAvro.getTotalAmount());
        orderHistory.setStatus(Arrays.asList(status));
        return orderHistory;
    }
}
