package io.github.victorjimenez.expense_tracker_api.events;

import io.github.victorjimenez.expense_tracker_api.models.BaseCard;

public class NonSufficientFundsEvent {
    private String message;

    public NonSufficientFundsEvent(String message, BaseCard card) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
