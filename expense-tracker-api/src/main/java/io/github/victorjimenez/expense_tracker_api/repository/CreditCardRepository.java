package io.github.victorjimenez.expense_tracker_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.micrometer.common.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;

public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    
    List<CreditCard> findAll(@NonNull Sort sort);
    CreditCard findByCardNumber(String cardNumber);
    List<CreditCard> findByCardProvider(String cardProvider);
    List<CreditCard> findByCardProviderIn(List<String> cardProvider);
    List<CreditCard> findByBalance(double balance);
    List<CreditCard> findByBalanceBetween(double minBalance, double maxBalance);
    List<CreditCard> findByCreditLimit(double creditLimit);
    List<CreditCard> findByCreditLimitBetween(double minCreditLimit, double maxCreditLimit);
    List<CreditCard> findByInterestRate(double interestRate);
    List<CreditCard> findByInterestRateBetween(double minInterestRate, double maxInterestRate);
    List<CreditCard> findByMinimumPayment(double minimumPayment);
    List<CreditCard> findByMinimumPaymentBetween(double minMinimumPayment, double maxMinimumPayment);
    List<CreditCard> findByStartOfBillingCycleAfter(LocalDate startOfBillingCycle);
    @Query("SELECT c FROM CreditCard c JOIN c.creditFees cf WHERE KEY(cf) = :feeType ")
    List<CreditCard> findByCreditFeeType(@Param("feeType") String feeType);
    @Query("SELECT c FROM CreditCard c JOIN c.creditFees cf WHERE VALUE(cf) = :feeAmount ")
    List<CreditCard> findByCreditFeeAmount(@Param("feeAmount") double feeAmount);
    @Query("SELECT c FROM CreditCard c JOIN c.creditFees cf WHERE VALUE(cf) BETWEEN :minFeeAmount AND :maxFeeAmount ")
    List<CreditCard> findByCreditFeeAmountBetween(@Param("minFeeAmount") double minFeeAmount, @Param("maxFeeAmount") double maxFeeAmount);
    @Query("SELECT c FROM CreditCard c WHERE SIZE(c.expenseTypeToPayFor) >= :paramSize AND (SELECT COUNT(e.id) FROM c.expenseTypeToPayFor e WHERE e.id IN :expenseTypeIds) = :paramSize")
    List<CreditCard> findByAllExpenseTypes(@Param("expenseTypeCount") int expenseTypeCount, @Param("expenseTypeIds") List<Long> expenseTypeIds);
    List<CreditCard> findByExpenseTypeToPayFor_IdIn(List<Long> expenseTypeIds);
    List<CreditCard> findByEligibleExpenses_IdIn(List<Long> expenseIds);
    @Query("SELECT c FROM CreditCard c WHERE SIZE(c.eligibleExpenses) >= :paramSize AND (SELECT COUNT(e.id) FROM c.eligibleExpenses e WHERE e.id IN :expenseIds) = :paramSize")
    List<CreditCard> findByAllExpenses(@Param("expenseCount") int expenseCount, @Param("expenseIds") List<Long> expenseIds);
}
