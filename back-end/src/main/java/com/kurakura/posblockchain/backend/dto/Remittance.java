package com.kurakura.posblockchain.backend.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class Remittance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Min(value = 1)
    private int senderId;

    @Min(value = 1)
    private int recipientId;

    @Min(value = 1)
    private int amount;

    private LocalDateTime timestamp;

    private int senderPossession;

    private int recipientPossession;
}