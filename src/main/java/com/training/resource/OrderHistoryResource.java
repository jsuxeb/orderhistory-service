package com.training.resource;

import com.training.constant.Channel;
import com.training.exceptions.ErrorHandlerUtil;
import com.training.exceptions.OrdersNotFoundException;
import com.training.exceptions.SystemUnavailableException;
import com.training.model.OrderHistory;
import com.training.patterns.proxy.OrderHistoryFinderProxy;
import com.training.sevice.OrderHistoryService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/v1/api/orderhistory")
public class OrderHistoryResource {

    @Inject
    OrderHistoryService orderHistoryService;

    @Inject
    OrderHistoryFinderProxy proxy;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getOrderHistoryResource(OrderHistory orderHistory) {
        return orderHistoryService.saveOrderHistory(orderHistory)
                .onItem()
                .transform(s -> Response.accepted(s).status(Response.Status.ACCEPTED).build());
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> findOrderById(@PathParam("id") Long id,
                                       @QueryParam("channel") Channel channel) {
        return proxy.findByOrderId(id, channel)
                .onItem()
                .transform(s -> Response.ok(s).status(Response.Status.OK).build())
                .onFailure(OrdersNotFoundException.class)
                .recoverWithItem(e -> Response.status(Response.Status.NOT_FOUND)
                        .entity(ErrorHandlerUtil.createErrorMessage(e))
                        .build())
                .onFailure(SystemUnavailableException.class)
                .recoverWithItem(e -> Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity(ErrorHandlerUtil.createErrorMessage(e))
                        .build());
    }

    @GET
    @Path("/user/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> findOrderByUserId(@PathParam("id") Long id,
                                       @QueryParam("channel") Channel channel) {
        return proxy.findByUserId(id, channel)
                .onItem()
                .transform(s -> Response.ok(s).status(Response.Status.OK).build())
                .onFailure(OrdersNotFoundException.class)
                .recoverWithItem(e -> Response.status(Response.Status.NOT_FOUND)
                        .entity(ErrorHandlerUtil.createErrorMessage(e))
                        .build())
                .onFailure(SystemUnavailableException.class)
                .recoverWithItem(e -> Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity(ErrorHandlerUtil.createErrorMessage(e))
                        .build());
    }
}
