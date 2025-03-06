package io.github.victorjimenez.expense_tracker_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.victorjimenez.expense_tracker_api.models.CreditCard;
import io.micrometer.common.lang.NonNull;

import java.util.Set;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    @NonNull
    List<CreditCard> findAll(@NonNull Sort sort);
    CreditCard findByCardNumber(String cardNumber);
    Set<CreditCard> findByCardProvider(String cardProvider);
    Set<CreditCard> findByCardProviderIn(Set<String> cardProvider);
    Set<CreditCard> findByBalance(double balance);
    Set<CreditCard> findByBalanceBetween(double minBalance, double maxBalance);
    Set<CreditCard> findByCreditLimit(double creditLimit);
    Set<CreditCard> findByCreditLimitBetween(double minCreditLimit, double maxCreditLimit);
    Set<CreditCard> findByInterestRate(double interestRate);
    Set<CreditCard> findByInterestRateBetween(double minInterestRate, double maxInterestRate);
    Set<CreditCard> findByMinimumPayment(double minimumPayment);
    Set<CreditCard> findByMinimumPaymentBetween(double minMinimumPayment, double maxMinimumPayment);
    @Query("SELECT c FROM CreditCard c JOIN c.creditFees cf WHERE KEY(cf) = :feeType ")
    Set<CreditCard> findByCreditFeeType(@Param("feeType") String feeType);
    @Query("SELECT c FROM CreditCard c JOIN c.creditFees cf WHERE VALUE(cf) = :feeAmount ")
    Set<CreditCard> findByCreditFeeAmount(@Param("feeAmount") double feeAmount);
    @Query("SELECT c FROM CreditCard c JOIN c.creditFees cf WHERE VALUE(cf) BETWEEN :minFeeAmount AND :maxFeeAmount ")
    Set<CreditCard> findByCreditFeeAmountBetween(@Param("minFeeAmount") double minFeeAmount, @Param("maxFeeAmount") double maxFeeAmount);
    @Query("SELECT c FROM CreditCard c WHERE :expenseTypeCount = (SELECT COUNT(e.id) FROM c.expenseTypeToPayFor e WHERE e.id IN: expenseTypeIds)")
    Set<CreditCard> findByExpenseTypeToPayForCount(@Param("expenseTypeCount") Long expenseTypeCount, @Param("expenseTypeIds") Set<Long> expenseTypeIds);
    Set<CreditCard> findByExpenseTypeToPayFor_IdIn(Set<Long> expenseTypeIds);
}
