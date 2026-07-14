package com.bankSim.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.bankSim.model.Transfer;
import com.bankSim.dto.responses.TransferProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("SELECT t.id as id, t.fromAccountId as fromAccountId, t.toAccountId as toAccountId, " +
           "t.amount as amount, t.status as status, t.message as message, " +
           "t.createdAt as createdAt, t.transferType as transferType " +
           "FROM Transfer t " +
           "WHERE (t.fromAccountId IN :accountIds OR t.toAccountId IN :accountIds) " +
           "AND t.createdAt >= :fromDate AND t.createdAt <= :toDate")
    List<TransferProjection> findTransfersForAccountsInDateRange(
            @Param("accountIds") List<Long> accountIds,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);
}