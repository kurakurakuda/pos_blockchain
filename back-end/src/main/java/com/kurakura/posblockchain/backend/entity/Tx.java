package com.kurakura.posblockchain.backend.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Tx implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private byte[] hash;

    private int senderId;

    private int recipientId;

    private int amount;

    private LocalDateTime timestamp;

    private List<PurchaseHistory> products;

    public String createPlainText() {
        StringBuilder sb = new StringBuilder();
        final String slash = "/";
        sb.append(String.valueOf(this.getSenderId()));
        sb.append(slash);
        sb.append(String.valueOf(this.getRecipientId()));
        sb.append(slash);
        sb.append(String.valueOf(this.getAmount()));
        sb.append(slash);
        sb.append(this.getTimestamp().toString());
        sb.append(slash);
        if (Objects.equals(this.getProducts(), null)) {
            return sb.toString();
        }
        final String HYPHEN = "-";
        for (PurchaseHistory ph: this.getProducts()) {
            sb.append(String.valueOf(ph.getProductId()));
            sb.append(HYPHEN);
            sb.append(ph.getName());
            sb.append(HYPHEN);
            sb.append(String.valueOf(ph.getPrice()));
            sb.append(HYPHEN);
            sb.append(String.valueOf(ph.getCount()));
            sb.append(slash);
        }
        return sb.toString();
    }
}