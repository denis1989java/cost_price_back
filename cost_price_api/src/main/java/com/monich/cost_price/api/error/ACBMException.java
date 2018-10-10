package com.monich.cost_price.api.error;

import lombok.Getter;

public class ACBMException extends RuntimeException {

    @Getter
    private final ErrorCode errorCode;

    @Getter
    private final  Object [] args;

    public ACBMException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
        args = null;
    }

    public ACBMException(ErrorCode errorCode, Object [] args) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.args = args;
    }
}
