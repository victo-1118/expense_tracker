package io.github.victorjimenez.expense_tracker_api.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fasterxml.jackson.databind.JsonSerializable.Base;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.BaseCardFeesType;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;
import java.util.List;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import org.springframework.lang.NonNull;
public interface BaseCardRepository extends JpaRepository<BaseCard, String> {
    @NonNull
    List<BaseCard> findAll(@NonNull Sort sort);
    Set <BaseCard> findByCardProvider(String cardProvider);
    Set <BaseCard> findByBalance(double balance);
    Set <BaseCard> findByBalanceBetween(double minBalance, double maxBalance);

    @Query("SELECT b FROM BaseCard b JOIN b.baseFees bf WHERE KEY(bf) = :feeType ")
    Set <BaseCard> findByBaseFeeType(@Param("feeType") BaseCardFeesType feeType);
    @Query("SELECT b FROM BaseCard b JOIN b.baseFees bf WHERE VALUE(bf) = :feeAmount ")
    Set <BaseCard> findByBaseFeeAmount(@Param("feeAmount") double feeAmount);
    @Query("SELECT b FROM BaseCard b JOIN b.baseFees bf WHERE VALUE(bf) BETWEEN :minFeeAmount AND :maxFeeAmount ")
    Set <BaseCard> findByBaseFeeAmountBetween(@Param("minFeeAmount") double minFeeAmount, @Param("maxFeeAmount") double maxFeeAmount);
    Set<BaseCard> findByExpenseTypeToPayFor_IdIn(Set<Long> expenseTypeIds);
    @Query("SELECT b FROM BaseCard b WHERE SIZE(b.expenseTypeToPayFor) >= :minSize AND NOT EXISTS (SELECT et FROM ExpenseTypeEntity et WHERE et.id IN :expenseTypeIds AND et NOT MEMBER OF b.expenseTypeToPayFor)")
    Set<BaseCard> findByAllExpenseTypes(@Param("expenseTypeIds") Set<Long> expenseTypeIds, @Param("minSize") int minSize);

    Set<BaseCard> findByEligibleExpenses_IdIn(Set<Long> expenseIds);
    @Query("SELECT b FROM BaseCard b WHERE SIZE(b.eligibleExpenses) >= :minSize AND NOT EXISTS (SELECT e FROM Expense e WHERE e.id IN :expenseIds AND e NOT MEMBER OF b.eligibleExpenses)")
    Set<BaseCard> findByAllExpenses(@Param("expenseIds") Set<Long> expenseIds, @Param("minSize") int minSize);
    BaseCard findByCardNumber(String cardNumber);
    
}
