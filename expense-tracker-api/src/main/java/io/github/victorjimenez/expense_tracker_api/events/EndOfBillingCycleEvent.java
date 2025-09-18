package io.github.victorjimenez.expense_tracker_api.events;

public class EndOfBillingCycleEvent {
    private String cardNumber;

    public EndOfBillingCycleEvent(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
}
