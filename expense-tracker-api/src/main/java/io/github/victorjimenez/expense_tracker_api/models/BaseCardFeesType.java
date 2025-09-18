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
    public static BaseCardFeesType fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null; // Or throw an exception, depending on your needs.
        }
        for (BaseCardFeesType feeType : BaseCardFeesType.values()) {
            if (feeType.getDisplayName().equalsIgnoreCase(text)) {
                return feeType;
            }
        }
        
        throw new IllegalArgumentException("Invalid BaseCardFeesType: " + text + ""); // Or throw new IllegalArgumentException("Invalid BaseCardFeesType: " + text);
    }
}
