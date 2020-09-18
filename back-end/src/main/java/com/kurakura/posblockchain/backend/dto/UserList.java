package com.kurakura.posblockchain.backend.dto;

import java.io.Serializable;
import java.util.List;

import com.kurakura.posblockchain.backend.entity.User;

import lombok.Data;

@Data
public class UserList implements Serializable {

    private static final long serialVersionUID = 1L;

    private int count;

    private List<User> users;
}