package com.training.sevice.impl;

import com.training.dto.OrderHistoryDto;
import com.training.exceptions.OrdersNotFoundException;
import com.training.exceptions.SystemUnavailableException;
import com.training.model.OrderHistory;
import com.training.model.Status;
import com.training.repository.OrderHistoryRepository;
import com.training.sevice.OrderHistoryService;
import com.training.util.ObjectMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.time.Instant;
import java.time.ZoneId;

@ApplicationScoped
public class OrderHistoryServiceImpl implements OrderHistoryService {

    private static final Log log = LogFactory.getLog(OrderHistoryServiceImpl.class);
    @Inject
    OrderHistoryRepository repository;

    @Override
    public Uni<OrderHistory> saveOrderHistory(OrderHistory orderHistory) {
        return repository.findByOrderId(orderHistory.getId())
                .onItem().ifNotNull().transform(order -> {
                    Status status = orderHistory.getStatus().get(0);
                    status.setCreatedAt(status.getCreatedAt());
                    order.getStatus()
                            .add(status);
                    return repository.update(order);
                })
                .onItem()
                .ifNull()
                .continueWith(() -> {
                    orderHistory.setCreatedAt(orderHistory.getCreatedAt().atZone(ZoneId.of("America/Lima")).toInstant());
                    return repository.save(orderHistory);
                })
                .flatMap(f -> f);
    }

    @Override
    @CircuitBreaker(
            requestVolumeThreshold = 4,
            failureRatio = 0.5,
            delay = 5000,
            skipOn = OrdersNotFoundException.class
    )
    @Timeout(5000)
    @Fallback(
            fallbackMethod = "fallbackX",
            skipOn = OrdersNotFoundException.class
    )
    public Uni<OrderHistoryDto> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId)
                .onItem()
                .ifNull()
                .failWith(() -> new OrdersNotFoundException("No se encontró el historial con order ID: " + orderId))
                .onItem()
                .ifNotNull()
                .transform(ObjectMapper::toOrderHistoryDto);

    }


    public Uni<OrderHistoryDto> fallbackX(Long orderId, Throwable throwable) {
        OrderHistoryDto fallbackOrder = new OrderHistoryDto();
        fallbackOrder.setStatusMessage("SERVICIO_NO_DISPONIBLE");
        fallbackOrder.setOrderId(orderId);
        return Uni.createFrom().failure(() -> new SystemUnavailableException("El servicio no se encuentra disponible, intente en unos minutos por favor"));
        //return Uni.createFrom().item(fallbackOrder);
    }


}
