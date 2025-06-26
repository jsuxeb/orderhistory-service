package com.training.resource;

import com.training.model.OrderHistory;
import com.training.sevice.OrderHistoryService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/v1/api/orderhistory")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class OrderHistoryResource {

    @Inject
    OrderHistoryService orderHistoryService;


    @POST
    public Uni<Response> getOrderHistoryResource(OrderHistory orderHistory) {
        return orderHistoryService.saveOrderHistory(orderHistory)
                .onItem()
                .transform(s -> Response.accepted(s).status(Response.Status.ACCEPTED).build());
    }
}
