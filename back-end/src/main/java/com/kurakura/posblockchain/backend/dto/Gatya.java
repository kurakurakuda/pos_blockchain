package com.kurakura.posblockchain.backend.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class Gatya implements Serializable {

    private static final long serialVersionUID = 1L;

    @Min(value = 1)
    private int recipientId;

    @Min(value = 1)
    private int amount;

    private int possession;

    private LocalDateTime timestamp;
}