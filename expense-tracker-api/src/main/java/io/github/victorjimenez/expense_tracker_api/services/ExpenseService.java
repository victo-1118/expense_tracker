package io.github.victorjimenez.expense_tracker_api.services;
import io.github.victorjimenez.expense_tracker_api.models.Expense;
import io.github.victorjimenez.expense_tracker_api.dto.ExpenseDTO;
import io.github.victorjimenez.expense_tracker_api.models.BaseCard;
import io.github.victorjimenez.expense_tracker_api.models.ExpenseTypeEntity;
import io.github.victorjimenez.expense_tracker_api.models.PaymentHistory;

import java.util.List;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
public interface ExpenseService {
    // Main Create Operation
    Expense create(ExpenseDTO expenseDTO);
    // Read Operations
    List<Expense> findByExpenseType_Id(Long expenseTypeId);
    List<Expense> findByExpenseAmount(double expenseAmount);
    List<Expense> findByExpenseAmountBetween(double minAmount, double maxAmount);
    List<Expense> findByExpenseDate(LocalDate expenseDate);
    List<Expense> findByExpenseDateBetween(LocalDate minDate, LocalDate maxDate);
    List<Expense> findByExpenseName(String expenseName);
    List<Expense> findByEligibleCards_CardNumber(List<String> cardNumber);
    List<Expense> findByExpenseDateGreaterThanEqualAndExpenseType_IdIn(LocalDate startDate, List<Long> expenseTypeIds);
    List<Expense> findByExpenseDateGreaterThanEqual(LocalDate startDate);

    // Write Operations
    Expense update(ExpenseDTO expenseDTO);
    void delete(Long expenseId);
    void addEligibleCard(BaseCard baseCard, List<Expense> expenses);
    void addEligibleCards(Expense expense, ExpenseTypeEntity expenseTypeEntity);
    void setExpenseAmount(Long expenseId, double expenseAmount);
    void setExpenseDate(Long expenseId, LocalDate expenseDate);
    void payingExpenseAmount(double amount, Expense expense);
    void removeEligibleCard(BaseCard card, List<Long> expenseTypeId);
    void addPaymentHistory(PaymentHistory paymentHistory, Expense expense);
    void removePaymentHistory(PaymentHistory paymentHistory, Expense expense);
    
    

    // Business Logic
    // Issue would be that if i try to find reoccuring expenses and ive already created an expense to recreate a reocurring expense
    // I would see two reoccurring expenses even if there should only be one. I guess i could fix this by checking frequency and todays
    //  date. however over time this would become inefficient. Or i could just set reoccuring to false and that fixes everything
    // Alternatively i could just reset the same expense object. I think this is a better solution since if i create more expenses
    // than that is wasting space and cards would have an old reoccuring expense.
    void updateReoccuringExpense(Expense expense);
    void checkForUpdateReoccurringExpense(LocalDate dateToday);
    // this should take into account the deletion or the removal of a expense type
    // lets keep this simple so no need to track what type these expenses had before.
    void nullifyExpenseType(Long expenseTypeId);

    

}
