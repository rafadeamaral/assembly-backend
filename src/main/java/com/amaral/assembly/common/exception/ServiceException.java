package com.amaral.assembly.common.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private Object[] params;

    public ServiceException(String message, Object... args) {
        super(message);
        params = args;
    }

}
