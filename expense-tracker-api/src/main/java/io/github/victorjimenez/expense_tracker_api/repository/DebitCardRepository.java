package io.github.victorjimenez.expense_tracker_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.victorjimenez.expense_tracker_api.models.DebitCard;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.micrometer.common.lang.NonNull;

import java.util.Set;
import java.util.List;
import org.springframework.data.domain.Sort;
public interface DebitCardRepository extends JpaRepository<DebitCard, String>  {
    List<DebitCard> findAll(@NonNull Sort sort);
    DebitCard findByCardNumber(String cardNumber);
    Set<DebitCard> findByCardProvider(String cardProvider);
    Set<DebitCard> findByBalance(double balance);
    Set<DebitCard> findByBalanceBetween(double minBalance, double maxBalance);
    Set<DebitCard> findByBudget(double budget);
    Set<DebitCard> findByBudgetBetween(double minBudget, double maxBudget);
    @Query("SELECT d FROM DebitCard d JOIN d.debitFees df WHERE KEY(df) = :feeType ")
    Set<DebitCard> findByDebitFeeType(@Param("feeType") ExpenseTypeEntity feeType);
    @Query("SELECT d FROM DebitCard d JOIN d.debitFees df WHERE VALUE(df) = :feeAmount ")
    Set<DebitCard> findByDebitFeeAmount(@Param("feeAmount") double feeAmount);
    @Query("SELECT d FROM DebitCard d JOIN d.debitFees df WHERE VALUE(df) BETWEEN :minFeeAmount AND :maxFeeAmount ")
    Set<DebitCard> findByDebitFeeAmountBetween(@Param("minFeeAmount") double minFeeAmount, @Param("maxFeeAmount") double maxFeeAmount);
    @Query("SELECT d FROM DebitCard d WHERE :expenseTypeCount = (SELECT COUNT(e.id) FROM d.expenseTypeToPayFor e WHERE e.id IN: expenseTypeIds)")
    Set<DebitCard> findByExpenseTypeToPayForCount(@Param("expenseTypeCount") Long expenseTypeCount, @Param("expenseTypeIds") Set<Long> expenseTypeIds);
    Set<DebitCard> findByExpenseTypeToPayFor_IdIn(Set<Long> expenseTypeIds);

    
}

