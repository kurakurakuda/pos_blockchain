package com.kurakura.posblockchain.infra.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMsg implements Serializable{

    private static final long serialVersionUID = 1L;

    private String errorCode;

    private String errorDescription;

}