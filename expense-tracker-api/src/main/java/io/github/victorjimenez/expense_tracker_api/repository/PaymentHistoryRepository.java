package io.github.victorjimenez.expense_tracker_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;
import java.util.List;
import java.time.LocalDate;


public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    List<PaymentHistory> findByAmountPaid(double amountPaid);
    List<PaymentHistory> findByPaymentDate(LocalDate datePaid);
    
} 
