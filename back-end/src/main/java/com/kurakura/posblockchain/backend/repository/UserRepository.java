package com.kurakura.posblockchain.backend.repository;

import java.util.List;

import com.kurakura.posblockchain.backend.entity.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRepository {

    @Insert("INSERT INTO users ("
        + "name, "
        + "public_key, "
        + "private_key, "
        + "node_address) "
        + "VALUES("
        + "#{name}, "
        + "#{publicKey}, "
        + "#{privateKey}, "
        + "#{nodeAddress})")
    @Options(useGeneratedKeys=true, keyColumn="id", keyProperty="id")
    public int insertUser(User user);

    @Insert("INSERT INTO users ("
        + "id, "
        + "name, "
        + "public_key, "
        + "private_key, "
        + "node_address, "
        + "is_administrator)"
        + "VALUES("
        + "#{id}, "
        + "#{name}, "
        + "#{publicKey}, "
        + "#{privateKey}, "
        + "#{nodeAddress}, "
        + "#{isAdministrator})")
    @Options(keyColumn="id", keyProperty="id")
    public int insertInitialUser(User user);

    @Select("SELECT EXISTS(SELECT * FROM users WHERE id=#{id})")
    public int isUserExist(int id);

    @Select("SELECT private_key from users WHERE id=#{id}")
    public String retrievePrivateKeyByUserId(int id);

    @Select("SELECT * FROM users WHERE id<>-1 ORDER BY id ASC")
    @Results(id = "user" ,value = {
        @Result(column = "id", property = "id"),
        @Result(column = "name", property = "name"),
        @Result(column = "public_key", property = "publicKey"),
        @Result(column = "private_key", property = "privateKey"),
        @Result(column = "node_address", property = "nodeAddress"),
        @Result(column = "is_administrator", property = "isAdministrator"),
    })
    public List<User> retrieveUserAndAdministrator();

    @Select("SELECT * FROM users WHERE id>=-1 ORDER BY id ASC")
    @ResultMap("user")
    public List<User> retrieveUserAndSystemUser();
}