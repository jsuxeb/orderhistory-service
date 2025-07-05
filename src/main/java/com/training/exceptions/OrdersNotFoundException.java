package com.training.exceptions;

import java.io.Serial;

public class OrdersNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public OrdersNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
