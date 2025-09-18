package io.github.victorjimenez.expense_tracker_api.models;

public enum DebitCardFeesType {
    OVERDRAFT_FEE("Overdraft Fee"),
    ATM_FEE("ATM Fee"),
    INSUFFICIENT_FUNDS_FEE("Non-Sufficient Funds Fee");

    private final String displayName;

    DebitCardFeesType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

    public static DebitCardFeesType fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null; // Or throw an exception, depending on your needs.
        }
        for (DebitCardFeesType feeType : DebitCardFeesType.values()) {
            if (feeType.getDisplayName().equalsIgnoreCase(text)) {
                return feeType;
            }
        }
        
        throw new IllegalArgumentException("Invalid DebitCardFeesType: " + text + ""); // Or throw new IllegalArgumentException("Invalid BaseCardFeesType: " + text);
    }
}
