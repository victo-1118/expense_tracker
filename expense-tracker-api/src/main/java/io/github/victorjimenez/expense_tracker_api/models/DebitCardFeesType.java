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
}
