package com.kurakura.posblockchain.backend.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Block implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte[] hash;

    private int index;

    private byte[] previousBlockHash;

    private int nonce;

    private LocalDateTime timestamp;

    private List<byte[]> txs;
}