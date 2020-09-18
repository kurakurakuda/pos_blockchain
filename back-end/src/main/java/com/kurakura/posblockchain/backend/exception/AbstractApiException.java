package com.kurakura.posblockchain.backend.exception;

import com.kurakura.posblockchain.infra.constants.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ErrorCode errorCode;

    private String errorDescription;

}