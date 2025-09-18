package io.github.victorjimenez.expense_tracker_api.warnings;

public class BudgetWarningException extends RuntimeException {
    public BudgetWarningException(String message) {
        super(message);
    }
}