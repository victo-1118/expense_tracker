package io.github.victorjimenez.expense_tracker_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import projection.CardUsageSummary;
import projection.ExpenseSummary;

import java.util.List;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    List<PaymentHistory> findByAmountPaid(double amountPaid);
    List<PaymentHistory> findByAmountPaidBetween(double minAmountPaid, double maxAmountPaid);   
    List<PaymentHistory> findByPaymentDate(LocalDate datePaid);
    List<PaymentHistory> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT et.expenseType as category, SUM(ph.amountPaid) as totalAmount, COUNT(ph) as paymentCount "+
        "FROM PaymentHistory ph JOIN ph.expensePaid e JOIN e.expenseType et GROUP BY et.expenseType ORDER BY totalAmount DESC",
        countQuery = "SELECT COUNT(DISTINCT et.expenseType) FROM PaymentHistory ph JOIN ph.expensePaid e JOIN e.expenseType et")
    Page<ExpenseSummary> summarizeExpensesType(Pageable pageable);
    @Query(value = "SELECT et.expenseType as category, SUM(ph.amountPaid) as totalAmount, COUNT(ph) as paymentCount "+
        "FROM PaymentHistory ph JOIN ph.expensePaid e JOIN e.expenseType et WHERE ph.paymentDate BETWEEN :startDate AND :endDate GROUP BY et.expenseType ORDER BY totalAmount DESC",
         countQuery = "SELECT COUNT(DISTINCT et.expenseType) FROM PaymentHistory ph JOIN ph.expensePaid e JOIN e.expenseType et WHERE ph.paymentDate BETWEEN :startDate AND :endDate")
    Page<ExpenseSummary> summarizeExpensesTypeBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    @Query("SELECT 'ALL' as category, SUM(ph.amountPaid) as totalAmount, COUNT(ph) as paymentCount " +
        "FROM PaymentHistory ph WHERE ph.paymentDate BETWEEN :startDate AND :endDate")
    ExpenseSummary summarizeExpensesBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query(value="SELECT ph.paidByCard.cardNumber as cardNumber, SUM(ph.amountPaid) as totalAmount, COUNT(ph) as transactionCount " +
        "FROM PaymentHistory ph GROUP BY ph.paidByCard.cardNumber ORDER BY totalAmount DESC",
        countQuery = "SELECT COUNT(DISTINCT ph.paidByCard.cardNumber) FROM PaymentHistory ph")
    Page<CardUsageSummary> summarizeCardUsage(Pageable pageable);
    @Query(value="SELECT ph.paidByCard.cardNumber as cardNumber, SUM(ph.amountPaid) as totalAmount, COUNT(ph) as transactionCount " +
        "FROM PaymentHistory ph WHERE ph.paymentDate BETWEEN :startDate AND :endDate GROUP BY ph.paidByCard.cardNumber ORDER BY totalAmount DESC",
        countQuery = "SELECT COUNT(DISTINCT ph.paidByCard.cardNumber) FROM PaymentHistory ph WHERE ph.paymentDate BETWEEN :startDate AND :endDate")
    Page<CardUsageSummary> summarizeCardUsageBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

    @Query(value="SELECT ph.paidByCard.cardNumber as cardNumber, AVG(ph.amountPaid) as avgTransaction,  COUNT(ph) as transactionCount " +
       "FROM PaymentHistory ph " +
       "GROUP BY ph.paidByCard.cardNumber " +
       "ORDER BY avgTransaction DESC",
       countQuery = "SELECT COUNT(DISTINCT ph.paidByCard.cardNumber) FROM PaymentHistory ph")
    Page<CardUsageSummary> findAverageTransactionAmountPerCard(Pageable pageable);
    @Query(value="SELECT ph.paidByCard.cardNumber as cardNumber, AVG(ph.amountPaid) as avgTransaction,  COUNT(ph) as transactionCount " +
       "FROM PaymentHistory ph " +
       "WHERE ph.paymentDate BETWEEN :startDate AND :endDate " +
       "GROUP BY ph.paidByCard.cardNumber " +
       "ORDER BY avgTransaction DESC",
       countQuery = "SELECT COUNT(DISTINCT ph.paidByCard.cardNumber) FROM PaymentHistory ph WHERE ph.paymentDate BETWEEN :startDate AND :endDate")
    Page<CardUsageSummary> findAverageTransactionAmountPerCardBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
} 
