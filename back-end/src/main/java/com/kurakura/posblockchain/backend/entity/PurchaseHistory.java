package com.kurakura.posblockchain.backend.entity;

import java.io.Serializable;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class PurchaseHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private byte[] hash;

    private int productId;

    private String name;

    @Min(value = 1)
    private int price;

    @Min(value = 1)
    private int count;
}