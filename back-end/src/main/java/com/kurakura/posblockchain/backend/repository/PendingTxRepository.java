package com.kurakura.posblockchain.backend.repository;

import java.util.List;

import com.kurakura.posblockchain.backend.entity.PendingTx;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PendingTxRepository {

    @Select("SELECT tx1.block_index, tx1.seq, tx1.tx_hash "
        + "FROM pending_tx AS tx1 "
        + "LEFT JOIN pending_tx AS tx2 "
        + "ON (tx1.block_index = tx2.block_index AND tx1.seq < tx2.seq) "
        + "WHERE tx2.seq IS NULL "
        + "ORDER BY tx1.block_index")
    @Results(id = "pendingTx", value = {
        @Result(id = true, column = "block_hash", property = "blockHash"),
        @Result(id = true, column = "seq", property = "seq"),
        @Result(id = true, column = "tx_hash", property = "txHash")
    })
    List<PendingTx> getLastSeqTxs();

    @Insert("INSERT INTO pending_tx ("
        + "block_index, "
        + "seq, "
        + "tx_hash) "
        + "VALUES("
        + "#{blockIndex}, "
        + "#{seq}, "
        + "#{txHash}) ")
    int insertPendingTx(PendingTx tx);

    @Select("SELECT block_index, seq, tx_hash "
        + "FROM pending_tx "
        + "WHERE block_index=#{index} "
        + "ORDER BY block_index")
    @ResultMap("pendingTx")
    List<PendingTx> retrievePendingTxsByBlockIndex(int index);
}