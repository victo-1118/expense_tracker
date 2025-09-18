package io.github.victorjimenez.expense_tracker_api.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fasterxml.jackson.databind.JsonSerializable.Base;

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
    List <BaseCard> findByCardProvider(String cardProvider);
    List <BaseCard> findByBalance(double balance);
    List <BaseCard> findByBalanceBetween(double minBalance, double maxBalance);

    @Query("SELECT b FROM BaseCard b JOIN b.baseFees bf WHERE KEY(bf) = :feeType ")
    List <BaseCard> findByBaseFeeType(@Param("feeType") String feeType);
    @Query("SELECT b FROM BaseCard b JOIN b.baseFees bf WHERE VALUE(bf) = :feeAmount ")
    List <BaseCard> findByBaseFeeAmount(@Param("feeAmount") double feeAmount);
    @Query("SELECT b FROM BaseCard b JOIN b.baseFees bf WHERE VALUE(bf) BETWEEN :minFeeAmount AND :maxFeeAmount ")
    List <BaseCard> findByBaseFeeAmountBetween(@Param("minFeeAmount") double minFeeAmount, @Param("maxFeeAmount") double maxFeeAmount);
    List<BaseCard> findByExpenseTypeToPayFor_IdIn(List<Long> expenseTypeIds);
    @Query("SELECT DISTINCT b FROM BaseCard b " +
       "JOIN b.expenseTypeToPayFor et " +
       "WHERE SIZE(b.expenseTypeToPayFor) >= :minSize " +
       "AND et.id IN :expenseTypeIds " +
       "GROUP BY b " +
       "HAVING COUNT(DISTINCT et.id) = :minSize")
    List<BaseCard> findByAllExpenseTypes(@Param("expenseTypeIds") List<Long> expenseTypeIds, 
                                   @Param("minSize") int minSize);

    List<BaseCard> findByEligibleExpenses_IdIn(List<Long> expenseIds);
    /**
     * Finds all BaseCards that have all the given expenseIds and has a list size of at least minSize
     * @param expenseIds a list of expense type ids
     * @param minSize the minimum size of the expenseTypeToPayFor list
     * @return a list of BaseCards that satisfy the condition
     */
    @Query("SELECT DISTINCT b FROM BaseCard b " +
       "JOIN b.eligibleExpenses e " +
       "WHERE SIZE(b.eligibleExpenses) >= :minSize " +
       "AND e.id IN :expenseIds " +
       "GROUP BY b " +
       "HAVING COUNT(DISTINCT e.id) = :minSize")
    List<BaseCard> findByAllExpenses(@Param("expenseIds") List<Long> expenseIds, @Param("minSize") int minSize);
    BaseCard findByCardNumber(String cardNumber);
    
}
