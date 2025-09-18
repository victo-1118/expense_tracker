package io.github.victorjimenez.expense_tracker_api.services;

import java.time.LocalDate;

import io.github.victorjimenez.expense_tracker_api.models.Expense;

public interface ExpensePaymentService {
    Boolean approveExpensePaymentCategory(String baseCardNumber, Long expenseId);
    void payForExpense(String baseCardNumber, Long expenseId, double amount, Boolean ignoreWarning);
    
} 
