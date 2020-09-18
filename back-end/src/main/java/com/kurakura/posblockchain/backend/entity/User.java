package com.kurakura.posblockchain.backend.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

	private int id;

    private String name;

    @JsonIgnore
    private String publicKey;

    @JsonIgnore
    private String privateKey;

    private int nodeAddress;

    @JsonIgnore
    private boolean isAdministrator;

}