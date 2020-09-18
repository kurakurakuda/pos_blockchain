package com.kurakura.posblockchain.backend.exception;

import com.kurakura.posblockchain.infra.constants.ErrorCode;

public class BadRequestParameterException extends AbstractApiException {

    private static final long serialVersionUID = 1L;

	public BadRequestParameterException(ErrorCode errorCode, String errorDescription) {
        super(errorCode, errorDescription);    
    }

}