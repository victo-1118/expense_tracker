package io.github.victorjimenez.expense_tracker_api.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import java.util.Set;
import java.time.LocalDate;
public interface ExpenseRepository extends JpaRepository<Expense, Long>{
    Set<Expense> findByExpenseType_Id(Long expenseTypeId);
    Set<Expense> findByExpenseAmount(double expenseAmount);
    Set<Expense> findByExpenseAmountBetween(double minAmount, double maxAmount);
    Set<Expense> findByExpenseDate(LocalDate expenseDate);
    Set<Expense> findByExpenseDateBetween(LocalDate minDate, LocalDate maxDate);
    Set<Expense> findByExpenseName(String expenseName);
    Set<Expense> findByEligibleCards_CardNumber(String cardNumber);



}
