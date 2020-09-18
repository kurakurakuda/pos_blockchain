package com.kurakura.posblockchain.backend.dto;

import java.io.Serializable;
import java.util.List;

import com.kurakura.posblockchain.backend.entity.Tx;

import lombok.Data;

@Data
public class TxList implements Serializable {

    private static final long serialVersionUID = 1L;

    private int userId;

    private int count;

    private int possession;

    private List<Tx> txs;
    
}