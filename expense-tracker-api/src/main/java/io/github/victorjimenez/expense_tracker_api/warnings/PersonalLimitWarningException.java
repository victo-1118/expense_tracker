package io.github.victorjimenez.expense_tracker_api.warnings;

public class PersonalLimitWarningException extends RuntimeException{
    public PersonalLimitWarningException(String message) {
        super(message);
    }
}
