package com.kurakura.posblockchain.backend.service;

import java.util.List;
import java.util.Objects;

import com.kurakura.posblockchain.backend.entity.PendingTx;
import com.kurakura.posblockchain.backend.exception.BusinessLogicFailureException;
import com.kurakura.posblockchain.backend.repository.PendingTxRepository;
import com.kurakura.posblockchain.infra.constants.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PendingTxService {

    @Autowired
    private PendingTxRepository pendingTxRepository;

    public PendingTx retrieveLastPendingTx() {
        List<PendingTx> pendingTxs = pendingTxRepository.getLastSeqTxs();
        if (pendingTxs.isEmpty()) {
            return null;
        }
        return pendingTxs.get(pendingTxs.size() - 1);
    }

    public PendingTx insertPendingTx(PendingTx lastTx, byte[] hash) {
        PendingTx newTx = new PendingTx();
        if (Objects.equals(lastTx, null)) {
            newTx.setBlockIndex(1);
            newTx.setSeq(1);
        } else if (lastTx.getSeq() == 10) {
            newTx.setBlockIndex(lastTx.getBlockIndex() + 1);
            newTx.setSeq(1);
        } else {
            newTx.setBlockIndex(lastTx.getBlockIndex());
            newTx.setSeq(lastTx.getSeq() + 1);
        }
        newTx.setTxHash(hash);
        int result = pendingTxRepository.insertPendingTx(newTx);
        if (result == 1) {
            return newTx;
        } else {
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, "Failed to insert pendingTx.");
        }
    }
}