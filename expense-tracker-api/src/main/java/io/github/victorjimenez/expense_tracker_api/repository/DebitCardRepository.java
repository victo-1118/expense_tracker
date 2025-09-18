package io.github.victorjimenez.expense_tracker_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.victorjimenez.expense_tracker_api.models.DebitCard;
import io.github.victorjimenez.expense_tracker_api.models.DebitCardFeesType;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.github.victorjimenez.expense_tracker_api.models.BaseCardFeesType;
import io.micrometer.common.lang.NonNull;

import java.util.List;
import java.util.List;
import org.springframework.data.domain.Sort;
public interface DebitCardRepository extends JpaRepository<DebitCard, String>  {
    List<DebitCard> findAll(@NonNull Sort sort);
    DebitCard findByCardNumber(String cardNumber);
    List<DebitCard> findByCardProvider(String cardProvider);
    List<DebitCard> findByBalance(double balance);
    List<DebitCard> findByBalanceBetween(double minBalance, double maxBalance);
    List<DebitCard> findByBudget(double budget);
    List<DebitCard> findByBudgetBetween(double minBudget, double maxBudget);
    @Query("SELECT d FROM DebitCard d JOIN d.baseFees bf WHERE KEY(bf) = :feeType ")
    List<DebitCard> findByBaseFeeType(@Param("feeType") BaseCardFeesType feeType);
    @Query("SELECT d FROM DebitCard d JOIN d.baseFees bf WHERE VALUE(bf) = :feeAmount ")
    List<DebitCard> findByBaseFeeAmount(@Param("feeAmount") double feeAmount);
    @Query("SELECT d FROM DebitCard d JOIN d.baseFees bf WHERE VALUE(bf) BETWEEN :minFeeAmount AND :maxFeeAmount ")
    List<DebitCard> findByBaseFeeAmountBetween(@Param("minFeeAmount") double minFeeAmount, @Param("maxFeeAmount") double maxFeeAmount);
    @Query("SELECT d FROM DebitCard d JOIN d.debitFees df WHERE KEY(df) = :feeType ")
    List<DebitCard> findByDebitFeeType(@Param("feeType") String feeType);
    @Query("SELECT d FROM DebitCard d JOIN d.debitFees df WHERE VALUE(df) = :feeAmount ")
    List<DebitCard> findByDebitFeeAmount(@Param("feeAmount") double feeAmount);
    @Query("SELECT d FROM DebitCard d JOIN d.debitFees df WHERE VALUE(df) BETWEEN :minFeeAmount AND :maxFeeAmount ")
    List<DebitCard> findByDebitFeeAmountBetween(@Param("minFeeAmount") double minFeeAmount, @Param("maxFeeAmount") double maxFeeAmount);
    @Query("SELECT d FROM DebitCard d WHERE SIZE(d.expenseTypeToPayFor) >= :paramSize AND (SELECT COUNT(e.id) FROM d.expenseTypeToPayFor e WHERE e.id IN :expenseTypeIds) = :paramSize")
    List<DebitCard> findByAllExpenseTypes(@Param("expenseTypeIds") List<Long> expenseTypeIds, @Param("paramSize") int paramSize);
    List<DebitCard> findByExpenseTypeToPayFor_IdIn(List<Long> expenseTypeIds);
   
    List<DebitCard> findByEligibleExpenses_IdIn(List<Long> expenseIds);
    @Query("SELECT d FROM DebitCard d WHERE SIZE(d.eligibleExpenses) >= :paramSize AND (SELECT COUNT(e.id) FROM d.eligibleExpenses e WHERE e.id IN :expenseIds) = :paramSize")
    List<DebitCard> findByAllExpenses(@Param("expenseIds") List<Long> expenseIds, @Param("paramSize") int paramSize);
    
    
}

