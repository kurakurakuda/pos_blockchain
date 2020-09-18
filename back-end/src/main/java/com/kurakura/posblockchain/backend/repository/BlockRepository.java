package com.kurakura.posblockchain.backend.repository;

import java.util.List;

import com.kurakura.posblockchain.backend.entity.Block;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BlockRepository {

    @Insert("INSERT INTO block ("
        + "hash, "
        + "block_index, "
        + "previous_block_hash, "
        + "nonce, "
        + "timestamp)"
        + "VALUES("
        + "#{hash}, "
        + "#{index}, "
        + "#{previousBlockHash}, "
        + "#{nonce}, "
        + "#{timestamp})")
    int insertBlock(Block block);

    @Select("SELECT * FROM block WHERE block_index=(SELECT MAX(block_index) FROM block)")
    @Results(id = "block", value = {
        @Result(id = true, property = "hash", column = "hash"),
        @Result(id = true, property = "index", column = "block_index"),
        @Result(id = true, property = "previousBlockHash", column = "previous_block_hash"),
        @Result(id = true, property = "nonce", column = "nonce"),
        @Result(id = true, property = "timestamp", column = "timestamp"),
    })
    Block retrieveLastBlock();

    @Select("SELECT * FROM block ORDER BY block_index ASC")
    @ResultMap("block")
    List<Block> retrieveAllBlock();
}