package com.kurakura.posblockchain.backend.repository;

import java.util.List;

import com.kurakura.posblockchain.backend.entity.PurchaseHistory;
import com.kurakura.posblockchain.backend.entity.Tx;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TxRepository {

    @Insert("INSERT INTO tx ("
        + "hash, "
        + "sender_id, "
        + "recipient_id, "
        + "amount, "
        + "timestamp)"
        + "VALUES("
        + "#{hash}, "
        + "#{senderId}, "
        + "#{recipientId}, "
        + "#{amount}, "
        + "#{timestamp})")
    int insertTx(Tx tx);

    @Insert("INSERT INTO purchase_history ("
        + "hash, "
        + "product_id, "
        + "name, " 
        + "price, "
        + "count) "
        + "VALUES("
        + "#{hash}, "
        + "#{productId}, "
        + "#{name}, "
        + "#{price}, "
        + "#{count})")
    int insertPurchaseHistory(PurchaseHistory purchaseHistory);

    @Select("SELECT * FROM tx WHERE sender_id = #{id} OR recipient_id = #{id} ORDER BY timestamp DESC")
    @Results(id =  "tx" ,value = {
        @Result(id = true, column = "hash", property = "hash"),
        @Result(id = true, column = "sender_id", property = "senderId"),
        @Result(id = true, column = "recipient_id", property = "recipientId"),
        @Result(id = true, column = "amount", property = "amount"),
        @Result(id = true, column = "timestamp", property = "timestamp"),
        @Result(id = true, column = "hash", property = "products",
        many = @Many(select = "retrievePurchaseHistory"))
    })
    List<Tx> retrieveTxsByUser(int id);

    @Select("SELECT * FROM tx ORDER BY timestamp DESC")
    @ResultMap("tx")
    List<Tx> retrieveAllTx();

    @Select("SELECT * FROM purchase_history WHERE hash = #{hash}")
    @Results(value = {
        @Result(id = true, column = "hash", property = "hash"),
        @Result(id = true, column = "product_id", property = "productId"),
        @Result(id = true, column = "name", property = "name"),
        @Result(id = true, column = "price", property = "price"),
        @Result(id = true, column = "count", property = "count")
    })
    PurchaseHistory retrievePurchaseHistory(byte[] hash);
}