package com.training.util;

import com.training.dto.OrderHistoryDto;
import com.training.model.OrderHistory;
import com.training.model.Status;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ObjectMapper {
    private static final Log log = LogFactory.getLog(ObjectMapper.class);

    public static OrderHistory toOrderHistory(avro.model.OrderHistory orderAvro) {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setId(orderAvro.getId());
        orderHistory.setUserId(orderAvro.getUserId());
        orderHistory.setCreatedAt(orderAvro.getCreatedAt() != null ? orderAvro.getCreatedAt() : null);
        orderHistory.setUpdatedAt(orderAvro.getUpdateAt() != null ? orderAvro.getUpdateAt() : null);

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
        //if(status.getStatus().equalsIgnoreCase("CREATED"))

        Instant date = status.getStatus().equalsIgnoreCase("CREATED")
                ? orderAvro.getCreatedAt() : orderAvro.getUpdateAt();
        status.setCreatedAt(date);
        orderHistory.setTotalAmount(orderAvro.getTotalAmount());
        orderHistory.setStatus(Arrays.asList(status));
        return orderHistory;
    }

    public static OrderHistoryDto toOrderHistoryDto(OrderHistory orderHistory) {
        OrderHistoryDto dto = new OrderHistoryDto();
        dto.setCreatedAt(formatearFecha(orderHistory.getCreatedAt()));
        dto.setItems(orderHistory.getItems());
        dto.setStatus(getStatus(orderHistory.getStatus()));
        dto.setTotalAmount(orderHistory.getTotalAmount());
        dto.setOrderId(orderHistory.getId());

        return dto;
    }

    private static List<Status> getStatus(List<Status> status) {
        List<Status> ordenados = status.stream()
                .sorted(Comparator.comparing(Status::getCreatedAt).reversed())
                .collect(Collectors.toList());
        return ordenados;


    }

    private static String formatearFecha(Instant orderDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd 'de' MMMM 'del' yyyy 'a las' hh:mm a")
                    .withZone(ZoneOffset.UTC)
                    .withLocale(new Locale("es", "PE"));

            return formatter.format(orderDate).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
