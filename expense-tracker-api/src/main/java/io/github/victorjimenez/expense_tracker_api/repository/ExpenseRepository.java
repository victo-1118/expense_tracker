package io.github.victorjimenez.expense_tracker_api.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.victorjimenez.expense_tracker_api.models.Expense;
import java.util.List;
import java.time.LocalDate;
public interface ExpenseRepository extends JpaRepository<Expense, Long>{
    
    List<Expense> findByExpenseType_Id(Long expenseTypeId);
    List<Expense> findByExpenseType_IdIn(List<Long> expenseTypeIds);
    List<Expense> findByExpenseAmount(double expenseAmount);
    List<Expense> findByExpenseDateAndReoccurring(LocalDate expenseDate, boolean reoccuring);
    List<Expense> findByExpenseAmountBetween(double minAmount, double maxAmount);
    List<Expense> findByExpenseDate(LocalDate expenseDate);
    List<Expense> findByExpenseDateBetween(LocalDate minDate, LocalDate maxDate);
    List<Expense> findByExpenseName(String expenseName);
    List<Expense> findByEligibleCards_CardNumber(List<String> cardNumber);
    List<Expense> findByExpenseDateGreaterThanEqualAndExpenseType_IdIn(LocalDate startDate, List<Long> expenseTypeIds);
    List<Expense> findByExpenseDateGreaterThanEqual(LocalDate startDate);



}
