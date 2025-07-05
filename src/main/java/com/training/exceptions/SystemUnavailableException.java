package com.training.exceptions;

import java.io.Serial;

public class SystemUnavailableException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public SystemUnavailableException(String errorMessage) {
        super(errorMessage);
    }

}
