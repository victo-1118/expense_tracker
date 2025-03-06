package io.github.victorjimenez.expense_tracker_api.models;

public enum CreditCardFeesType {
    ANNUAL_FEE("Annual Fee"), 
    LATE_FEE("Late Fee"),
    OVERLIMIT_FEE("Over Limit Fee");

    private final String displayName;

    CreditCardFeesType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
