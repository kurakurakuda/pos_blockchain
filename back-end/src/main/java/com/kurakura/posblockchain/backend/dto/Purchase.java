package com.kurakura.posblockchain.backend.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.kurakura.posblockchain.backend.entity.PurchaseHistory;

import lombok.Data;

@Data
public class Purchase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Min(value = 1)
    private int senderId;

    private LocalDateTime timestamp;

    @NotEmpty
    private List<PurchaseHistory> products;

    private int possession;
}