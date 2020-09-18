package com.kurakura.posblockchain.backend.entity;

import lombok.Data;

@Data
public class PendingTx {

    private int blockIndex;

    private int seq;

    private byte[] txHash;
}