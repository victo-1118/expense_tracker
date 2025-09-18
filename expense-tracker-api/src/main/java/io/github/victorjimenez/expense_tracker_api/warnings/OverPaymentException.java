package io.github.victorjimenez.expense_tracker_api.warnings;

public class OverPaymentException extends RuntimeException {
    public OverPaymentException(String message) {
        super(message);
    }
}
