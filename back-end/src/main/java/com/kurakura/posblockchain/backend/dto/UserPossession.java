package com.kurakura.posblockchain.backend.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserPossession implements Serializable {

    private static final long serialVersionUID = 1L;

    private int userId;

    private int possession;
    
}