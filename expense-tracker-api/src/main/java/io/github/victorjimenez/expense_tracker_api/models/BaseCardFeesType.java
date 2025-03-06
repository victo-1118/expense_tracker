package io.github.victorjimenez.expense_tracker_api.models;

public enum BaseCardFeesType {
    FOREIGN_TRANSACTION_FEE("Foreign Transaction Fee"),
    CARD_REPLACEMENT_FEE("Card Replacement Fee");
    
    private final String displayName;
    BaseCardFeesType(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
