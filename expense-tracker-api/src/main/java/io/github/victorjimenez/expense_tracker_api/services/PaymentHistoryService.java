package io.github.victorjimenez.expense_tracker_api.services;
import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import io.github.victorjimenez.expense_tracker_api.dto.PaymentHistoryDTO;

import projection.*;
import java.util.List;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


public interface PaymentHistoryService {
    //Create Operation
    PaymentHistory create(PaymentHistoryDTO paymentHistoryDTO);
    //Read Operations
    List<PaymentHistory> findByAmountPaid(double amountPaid);
    List<PaymentHistory> findByPaymentDate(LocalDate paymentDate);
    List<PaymentHistory> findByAmountPaidBetween(double minAmountPaid, double maxAmountPaid);
    List<PaymentHistory> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
    //Write Operations

    void delete(Long id);
    //Business Logic

    // I guess i should work on frontend wire framing first.
    Page<ExpenseSummary> getExpenseSummaryPerCategory(Pageable pageable);
    Page<ExpenseSummary> getExpenseSummaryPerCategoryBetweenDates( LocalDate startDate,  LocalDate endDate, Pageable pageable);
    ExpenseSummary getExpenseSummaryBetweenDates( LocalDate startDate,  LocalDate endDate);
    Page<CardUsageSummary> getCardUsageSummary(Pageable pageable);
    Page<CardUsageSummary> getCardUsageSummaryBetweenDates( LocalDate startDate,  LocalDate endDate, Pageable pageable);
    Page<CardUsageSummary> getAverageTransactionAmountPerCard(Pageable pageable);
    Page<CardUsageSummary> getAverageTransactionAmountPerCardBetweenDates(LocalDate startDate,  LocalDate endDate, Pageable pageable);


}
