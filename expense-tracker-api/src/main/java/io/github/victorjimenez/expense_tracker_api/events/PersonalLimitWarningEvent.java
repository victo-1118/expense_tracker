package io.github.victorjimenez.expense_tracker_api.events;

public class PersonalLimitWarningEvent {
    private String message;

    public PersonalLimitWarningEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
