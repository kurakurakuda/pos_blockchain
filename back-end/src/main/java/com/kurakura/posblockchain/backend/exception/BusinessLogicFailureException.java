package com.kurakura.posblockchain.backend.exception;

import com.kurakura.posblockchain.infra.constants.ErrorCode;

public class BusinessLogicFailureException extends AbstractApiException {

    private static final long serialVersionUID = 1L;

    public BusinessLogicFailureException(ErrorCode errorCode, String errorDescription) {
        super(errorCode, errorDescription);    
    }

}